package com.bosch.epc.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author VFE1COB
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ALMRole {

    @XmlAttribute
    private String id;
    
    /**
     * @return the id
     */
    
    public String getId() {
      return id;
    }
    
    /**
     * @param id the id to set
     */
    public void setId(String id) {
      this.id = id;
    }
    
    /**
     * @return the permission
     */
   
    public String getPermission() {
      return permission;
    }
    
    /**
     * @param permission the permission to set
     */
    public void setPermission(String permission) {
      this.permission = permission;
    }
    @XmlAttribute
    private String permission;

    // Getters, Setters, etc.
}