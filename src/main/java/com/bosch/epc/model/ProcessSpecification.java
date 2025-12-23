package com.bosch.epc.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;


/**
 * @author VFE1COB
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ProcessSpecification {

    @XmlElement(name = "conditions")
    private Conditions conditions;

    // Getters and Setters
    /**
     * @return Conditions object
     */
    public Conditions getConditions() {
        return conditions;
    }

    /**
     * @param conditions Pass Conditions objects to set
     */
    public void setConditions(Conditions conditions) {
        this.conditions = conditions;
    }
}
