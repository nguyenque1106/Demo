package com.bosch.rtc.utils.templateexchange.processconfig.xml;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.bosch.rtc.utils.templateexchange.bean.ConditionBean;

/**
 * Pojo class that holds the custom condition definition existing in the process config file.
 */
public class ExistingCustomConditionDefinition {

  private List<ConditionBean> customConditionBeansCollection = new ArrayList<>();
  private Set<String> existingCustomConditionScriptNamesCollection = new HashSet<>();

  /**
   * Pojo class to hold intermediate results of collecting custom condition bean details from specification cml.
   *
   * @param customConditionBeansCollection - collection of existing custom condition beans, must not be null
   * @param existingCustomConditionScriptNamesCollection2 - collection of script names of existing custom condition
   *          defintions, must not be null
   */
  public ExistingCustomConditionDefinition(final List<ConditionBean> customConditionBeansCollection,
      final Set<String> existingCustomConditionScriptNamesCollection2) {
    this.customConditionBeansCollection = customConditionBeansCollection;
    this.existingCustomConditionScriptNamesCollection = existingCustomConditionScriptNamesCollection2;
  }

  /**
   * @return the customConditionBeansCollection
   */
  public List<ConditionBean> getCustomConditionBeansCollection() {
    return this.customConditionBeansCollection;
  }

  /**
   * @return the existingCustomConditionScriptNamesCollection
   */
  public Set<String> getExistingCustomConditionScriptNamesCollection() {
    return this.existingCustomConditionScriptNamesCollection;
  }
}