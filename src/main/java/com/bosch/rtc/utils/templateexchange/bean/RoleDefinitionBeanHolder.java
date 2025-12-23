/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.rtc.utils.templateexchange.bean;

import java.util.Set;

/**
 * @author VFE1COB
 *
 */
public class RoleDefinitionBeanHolder {


    final private Set<RoleDefinitionBean> roleXMLContents;
    


    /**
     * Parameterised Constructor.
     *
     * @param conditionXMLContents - the xml contents of the condition, must not be null
     * @param preConditionXMLContents - the xml contents of the pre-condition, must not be null
     */
    public RoleDefinitionBeanHolder(final Set<RoleDefinitionBean> roleXMLContents) {
      this.roleXMLContents = roleXMLContents;
      
    }



    /**
     * @return the roleXMLContents
     */
    public Set<RoleDefinitionBean> getRoleXMLContents() {
      return roleXMLContents;
    }

  

  }

