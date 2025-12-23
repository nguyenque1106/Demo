/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.model;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author ppt4kor
 */
@Component
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Role {

  @JsonIgnore
  private int id;
  private String name;
  private String permission;

  /**
   * Default constructor to fix Jackson deserialization issue
   */
  public Role() {

  }

  /**
   * @return the id
   */
  public int getId() {
    return this.id;
  }

  /**
   * @param id the id to set
   */
  public void setId(final int id) {
    this.id = id;
  }

  /**
   * @return the name
   */
  public String getName() {
    return this.name;
  }

  /**
   * @param name the name to set
   */
  public void setName(final String name) {
    this.name = name;
  }

  /**
   * @return the permission
   */
  public String getPermission() {
    return this.permission;
  }

  /**
   * @param permission the permission to set
   */
  public void setPermission(final String permission) {
    this.permission = permission;
  }

}
