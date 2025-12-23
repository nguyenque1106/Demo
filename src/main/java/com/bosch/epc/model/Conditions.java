package com.bosch.epc.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author VFE1COB
 *
 */
@XmlRootElement(name = "conditions")
public class Conditions {
  
    private List<XMLCondition> conditionList;

    // Getters and Setters
    @XmlElement(name = "condition")
    public List<XMLCondition> getConditionList() {
        return conditionList;
    }

    public void setConditionList(List<XMLCondition> conditionList) {
        this.conditionList = conditionList;
    }
}
