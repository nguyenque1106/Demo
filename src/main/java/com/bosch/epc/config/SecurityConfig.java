/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.firewall.DefaultHttpFirewall;

import waffle.servlet.spi.SecurityFilterProviderCollection;
import waffle.spring.NegotiateSecurityFilter;
import waffle.spring.NegotiateSecurityFilterEntryPoint;
import waffle.windows.auth.impl.WindowsAuthProviderImpl;


/**
 * This SecurityConfig class to authorize requests by using csrf-token
 *
 * @author QYU1HC
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(final HttpSecurity http) throws Exception {
    http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // CSRF token in a cookie
        .and().authorizeRequests().antMatchers("/security/csrf-token").permitAll()
        .antMatchers(HttpMethod.GET, "/epc/request", "/request/create", "/projectarea/getByLoggedInUser", "/user/**")
        .authenticated().antMatchers(HttpMethod.POST, "/epc/**", "/request/create-workon", "/request/create",
            "/request/update", "/attrperm/**")
        .authenticated().anyRequest().permitAll();
    http.csrf().disable();
    http.exceptionHandling().authenticationEntryPoint(negotiateSecurityFilterEntryPoint()).and()
        .addFilterBefore(negotiateSecurityFilter(), UsernamePasswordAuthenticationFilter.class);

  }


  /**
   * This filter will be executed before UsernamePasswordAuthenticationFilter of Spring security Adding the
   * WindowsAuthProviderImpl class to handle SSO.
   *
   * @return
   */
  @Bean
  NegotiateSecurityFilter negotiateSecurityFilter() {
    NegotiateSecurityFilter filter = new NegotiateSecurityFilter();
    filter.setAllowGuestLogin(false);
    filter.setProvider(new SecurityFilterProviderCollection(new WindowsAuthProviderImpl()));
    return filter;
  }

  /**
   * EntryPoint will handle exception if the exception occur while authentication. the WindowsAuthProviderImpl will be
   * used as re-try
   *
   * @return
   */
  @Bean
  NegotiateSecurityFilterEntryPoint negotiateSecurityFilterEntryPoint() {
    NegotiateSecurityFilterEntryPoint entrypoint = new NegotiateSecurityFilterEntryPoint();
    entrypoint.setProvider(new SecurityFilterProviderCollection(new WindowsAuthProviderImpl()));
    return entrypoint;
  }

  @Override
  public void configure(final WebSecurity web) throws Exception {
    web.httpFirewall(new DefaultHttpFirewall());
  }

}
