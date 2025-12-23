/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bosch.common.ldap.LdapUtil;
import com.bosch.common.ldap.model.LdapUser;
import com.bosch.epc.constant.CommonConstant;
import com.bosch.epc.constant.ToolRoleEnum;
import com.bosch.epc.model.LDAPUser;
import com.bosch.rtc.util.CryptoSupport;
import com.bosch.rtc.util.PropertyUtils;
import com.bosch.rtc.util.UserUtils;

import io.swagger.v3.oas.annotations.Operation;

/**
 * @author GHT9HC
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {

  private static final Logger logger = LoggerFactory.getLogger(UserController.class);
  

  /**
   * Method to get the logged in user profile
   * 
   * @return UserProfile
   */
  @GetMapping("/profile")
  @Operation(summary = "Get user details in active directory", description = "Get user details in active directory")
  public ResponseEntity<?> getUserProfile() {
    // Get the current authentication object
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || authentication.getPrincipal().equals("anonymousUser")) {
      // Handle cases where SSO simply failed (user is not authenticated at all)
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(new ErrorResponse("Authentication required via Windows SSO."));
    }

    // Extract authorities
    Collection<? extends GrantedAuthority> userAuthorities = authentication.getAuthorities();
    
    // Define a set of all valid application role strings from the enum
    Set<String> validAppRoleStrings = Arrays.stream(ToolRoleEnum.values())
                                            .map(ToolRoleEnum::getRoleId)
                                            .collect(Collectors.toSet());

    // Filter the user's authorities to only include those present in our enum list
    Set<String> matchedRoles = userAuthorities.stream()
        .map(GrantedAuthority::getAuthority)
        .filter(validAppRoleStrings::contains)
        .collect(Collectors.toSet());

    // Optional: Check if the user has any valid role to allow access to this endpoint
     if (matchedRoles.isEmpty()) {
         return ResponseEntity.status(HttpStatus.FORBIDDEN)
                              .body(new ErrorResponse("User " + authentication.getPrincipal().toString() + "  has no recognized application roles."));
    }
    
    // Success: return the profile with only the matched roles
    String username = authentication.getPrincipal().toString();
    
    // Return User details
    List<LdapUser> ldapUser = LdapUtil.searchByName(UserUtils.extractSimpleUsername(username));
    if (ldapUser.isEmpty()) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN)
          .body(new ErrorResponse("User " + authentication.getPrincipal().toString() + "  has not found in LDAP."));
    }
    
    return ResponseEntity.ok(new UserProfileResponse(username, ldapUser.get(0).getDisplayName(), matchedRoles));

  }

 //Helper class for standardized error responses
  private static class ErrorResponse {

    public String message;

    public ErrorResponse(String message) {
      this.message = message;
    }
  }

  // Helper class for standardized success responses
  private static class UserProfileResponse {

    public String username;
    public String displayName;
    public Collection<String> roles;

    public UserProfileResponse(String username, String displayName, Collection<String> roles) {
      this.username = username;
      this.displayName = displayName;
      this.roles = roles;
    }
  }

  
  /**
   * function to get ProcessOwners from LDAP
   * 
   * @return processOwnersNames
   */
  @GetMapping("/getProcessOwners")
  public List<String> getProcessOwners() {
    List<String> processOwnersNames = new ArrayList<>();
    List<String> domains = new ArrayList<>(Arrays.asList(CommonConstant.DOMAINS.split(",")));
    try {
      List<LDAPUser> usersList = getAllUsersOfGroup(ToolRoleEnum.PROCESS_OWNER.getRoleId(), domains,
          PropertyUtils.getPropValues("LADAP_USERNAME"),
          CryptoSupport.crypto(PropertyUtils.getPropValues("LADAP_PASSWORD")));
      usersList.forEach(user -> processOwnersNames.add(user.getDisplayName()));
    }
    catch (Exception exptn) {
      logger.error("Unable to fecth the ProcessOwners list from LDAP : " + exptn.getMessage());
    }
    return processOwnersNames;
  }


  /**
   * function to get LDAP users list using group name
   *
   * @param groupName
   * @param domains
   * @param ldapUsername
   * @param ldapPassword
   * @return users
   */
  public List<LDAPUser> getAllUsersOfGroup(String groupName, List<String> domains, String ldapUsername,
      String ldapPassword) {
    List<LDAPUser> users = new ArrayList<>();
    boolean groupFound = false;

    for (String domain : domains) {
      if (groupFound)
        break;

      try {
        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, CommonConstant.CONTEXT_FACTORY);
        env.put(Context.PROVIDER_URL, CommonConstant.LDAP_URL);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, ldapUsername);
        env.put(Context.SECURITY_CREDENTIALS, ldapPassword);
        DirContext ctx = new InitialDirContext(env);

        // Search for the group
        String searchFilter = "(&(objectClass=group)(cn=" + groupName + "))";
        SearchControls controls = new SearchControls();
        controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        NamingEnumeration<SearchResult> groups = ctx.search("", searchFilter, controls);

        if (!groups.hasMore())
          continue;
        groupFound = true;

        SearchResult groupResult = groups.next();
        Attributes groupAttributes = groupResult.getAttributes();
        Attribute members = groupAttributes.get("member");

        if (members != null) {
          NamingEnumeration<?> memberEnum = members.getAll();
          while (memberEnum.hasMore()) {
            String memberDN = (String) memberEnum.next();
            Attributes userAttrs = ctx.getAttributes(memberDN);
            if (userAttrs.get("objectClass").contains("user")) {
              LDAPUser user = new LDAPUser();
              user.setUserId(getAttr(userAttrs, "sAMAccountName"));
              user.setDisplayName(getAttr(userAttrs, "displayName"));
              user.setEmail(getAttr(userAttrs, "mail"));
              user.setFirstName(getAttr(userAttrs, "givenName"));
              user.setLastName(getAttr(userAttrs, "sn"));
              users.add(user);
            }
          }
        }
        ctx.close();
      }
      catch (Exception exptn) {
        logger.error("Failed to fetch LDAP group user details using Group name and Domain" + exptn.getMessage());
      }
    }
    return users;
  }

  /**
   * function to get Attributes using Attribute Name
   *
   * @param attrs
   * @param attrName
   * @return attr
   */
  private String getAttr(Attributes attrs, String attrName) {
    try {
      Attribute attr = attrs.get(attrName);
      return attr != null ? attr.get().toString() : null;
    }
    catch (Exception e) {
      return null;
    }
  }
}
