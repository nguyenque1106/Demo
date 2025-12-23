/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.rtc.utils.templateexchange.bean;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class is holding the condition and pre-condition bean instances which is used in different use cases.
 *
 * @author pla7kor
 */
@XmlRootElement(name = "conditions")

public class ConditionBeanHolder {
  private final Set<ConditionBean> conditionBeans;
  private final Set<PreConditionBean> preConditionBeans;
  private final Set<String> internalAttributeIds;

  /**
   * Parameterised Constructor.
   *
   * @param conditionBeans - collection of conditionBeans, must not be null.
   * @param preConditionBeans - collection of pre-conditionBeans, must not be null.
   * @param internalAttributeIds - collection of internal attribute ids, must not be null but may be empty.
   */
  public ConditionBeanHolder(final Collection<ConditionBean> conditionBeans,
      final Collection<PreConditionBean> preConditionBeans, final Set<String> internalAttributeIds) {
    this.conditionBeans = new HashSet<>(conditionBeans);
    this.preConditionBeans = new HashSet<>(preConditionBeans);
    this.internalAttributeIds = new HashSet<>(internalAttributeIds);
  }


  /**
   * @return the conditionBeans
   */
  public Set<ConditionBean> getConditionBeans() {
    return this.conditionBeans;
  }

  /**
   * @return the preConditionBeans
   */
  public Set<PreConditionBean> getPreConditionBeans() {
    return this.preConditionBeans;
  }


  /**
   * @return the internalAttributeIds
   */
  public Set<String> getInternalAttributeIds() {
    return this.internalAttributeIds;
  }
}
