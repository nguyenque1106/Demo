/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.model;

import org.springframework.stereotype.Component;

/**
 * @author HVO2KOR
 */
@Component
public class LDAPUser {

  private String UserId;
  private String Email;
  private String FirstName;
  private String LastName;
  private String DisplayName;
  
  /**
   * @return the userId
   */
  public String getUserId() {
    return UserId;
  }
  
  /**
   * @param userId the userId to set
   */
  public void setUserId(String userId) {
    UserId = userId;
  }
  
  /**
   * @return the email
   */
  public String getEmail() {
    return Email;
  }
  
  /**
   * @param email the email to set
   */
  public void setEmail(String email) {
    Email = email;
  }
  
  /**
   * @return the firstName
   */
  public String getFirstName() {
    return FirstName;
  }
  
  /**
   * @param firstName the firstName to set
   */
  public void setFirstName(String firstName) {
    FirstName = firstName;
  }
  
  /**
   * @return the lastName
   */
  public String getLastName() {
    return LastName;
  }
  
  /**
   * @param lastName the lastName to set
   */
  public void setLastName(String lastName) {
    LastName = lastName;
  }
  
  /**
   * @return the displayName
   */
  public String getDisplayName() {
    return DisplayName;
  }
  
  /**
   * @param displayName the displayName to set
   */
  public void setDisplayName(String displayName) {
    DisplayName = displayName;
  }



}
