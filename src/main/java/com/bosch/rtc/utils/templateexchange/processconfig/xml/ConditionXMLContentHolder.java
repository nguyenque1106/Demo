/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.rtc.utils.templateexchange.processconfig.xml;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is holding the condition and pre-condition xml contents which is used to merge in the process config file.
 *
 * @author pla7kor
 */
public class ConditionXMLContentHolder {

  final private String conditionXMLContents;
  final private List<String> preConditionXMLContents;


  /**
   * Parameterised Constructor.
   *
   * @param conditionXMLContents - the xml contents of the condition, must not be null
   * @param preConditionXMLContents - the xml contents of the pre-condition, must not be null
   */
  public ConditionXMLContentHolder(final String conditionXMLContents, final List<String> preConditionXMLContents) {
    this.conditionXMLContents = conditionXMLContents;
    this.preConditionXMLContents = new ArrayList<>(preConditionXMLContents);
  }

  /**
   * @return the conditionXMLContents
   */
  public String getConditionXMLContents() {
    return this.conditionXMLContents;
  }

  /**
   * @return the preConditionXMLContents
   */
  public List<String> getPreConditionXMLContents() {
    return this.preConditionXMLContents;
  }


}
