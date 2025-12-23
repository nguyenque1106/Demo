/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.model;


/**
 * @author VFE1COB
 *
 */
  public enum Permissions{
    READ("r"),
    WRITE("w"),
    MANDATORY("m"),
    READ_MANDATORY("r+m");

    private String value;
    private Permissions(String value)
    {
       this.value = value;
    }

    @Override
    public String toString()
    {
       return this.value; //This will return , # or +
    }
}

