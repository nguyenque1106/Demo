package com.bosch.epc.model;



import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;



/**
 * @author VFE1COB
 *
 */
public class XMLCondition {

  
    private String id;
 
    private String name;
   
    private String providerId;

    private List<WorkFlowProperties> workflowProperties;
    
    private String type;

    
    /**
     * @return the id
     */
    @XmlAttribute(name = "id")
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
     * @return the name
     */
    @XmlAttribute(name = "name")
    public String getName() {
      return name;
    }

    
    /**
     * @param name the name to set
     */
    public void setName(String name) {
      this.name = name;
    }

    
    /**
     * @return the providerId
     */
    @XmlAttribute(name = "providerId")
    public String getProviderId() {
      return providerId;
    }

    
    /**
     * @param providerId the providerId to set
     */
    public void setProviderId(String providerId) {
      this.providerId = providerId;
    }

    
    /**
     * @return the workflowProperties
     */
    
    public List<WorkFlowProperties> getWorkflowProperties() {
      return workflowProperties;
    }

    
    /**
     * @param workflowProperties the workflowProperties to set
     */
    public void setWorkflowProperties(List<WorkFlowProperties> workflowProperties) {
      this.workflowProperties = workflowProperties;
    }


    /**
     * @return the type
     */
    public String getType() {
      return type;
    }


    /**
     * @param type the type to set
     */
    public void setType(String type) {
      this.type = type;
    }

   
}