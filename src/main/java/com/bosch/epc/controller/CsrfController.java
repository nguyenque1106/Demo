/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.controller;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @author QYU1HC
 */
@RestController
@RequestMapping(value = "/security")
@Tag(name = "CSRF", description = "APIs for getting csrf token")
public class CsrfController {

  /**
   * function to get csrf-token
   *
   * @param token
   * @return
   */
  @Operation(summary = "Get Csrf token", description = "Retrieves a csrf token")
  @GetMapping("/csrf-token")
  public CsrfToken csrf(final CsrfToken token) {
    return token;
  }
}