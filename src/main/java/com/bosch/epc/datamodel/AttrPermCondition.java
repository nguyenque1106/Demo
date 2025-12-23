package com.bosch.epc.datamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;


/**
 * The persistent class for the attr_perm_condition database table.
 */
@Entity
@Table(name = "attr_perm_condition")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AttrPermCondition implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String attrid;
    private String attrname;

    @ManyToOne
    @JoinColumn(name = "pa_id", referencedColumnName = "id", nullable = false)
    private ProjectArea projectArea;

    @ManyToOne
    @JoinColumn(name = "req_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private Request request;

    @OneToMany(mappedBy = "attrPermCondition", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<AttrPermWorkflow> attrPermWorkflows = new ArrayList<>();
    
    /**
   * @param workflow
   */
  public void addWorkflow(AttrPermWorkflow workflow) {
      workflow.setAttrPermCondition(this);
      this.attrPermWorkflows.add(workflow);
  }

  /**
   * @param workflow
   */
  public void removeWorkflow(AttrPermWorkflow workflow) {
      workflow.setAttrPermCondition(null);
      this.attrPermWorkflows.remove(workflow);
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
   * @return attrPermWorkflows
   */
  public List<AttrPermWorkflow> getAttrPermWorkflows() {
    return this.attrPermWorkflows;
  }

  /**
   * @param attrPermWorkflows
   */
  public void setAttrPermWorkflows(final List<AttrPermWorkflow> attrPermWorkflows) {
    this.attrPermWorkflows = attrPermWorkflows;
  }

  /**
   * @return the attrid
   */
  public String getAttrid() {
    return this.attrid;
  }

  /**
   * @param attrid the attrid to set
   */
  public void setAttrid(final String attrid) {
    this.attrid = attrid;
  }

  /**
   * @return the attrname
   */
  public String getAttrname() {
    return this.attrname;
  }


  /**
   * @param attrname the attrname to set
   */
  public void setAttrname(final String attrname) {
    this.attrname = attrname;
  }


  /**
   * @return the projectArea
   */
  public ProjectArea getProjectArea() {
    return this.projectArea;
  }


  /**
   * @param projectArea the projectArea to set
   */
  public void setProjectArea(final ProjectArea projectArea) {
    this.projectArea = projectArea;
  }


  /**
   * @return the request
   */
  public Request getRequest() {
    return this.request;
  }


  /**
   * @param request the request to set
   */
  public void setRequest(final Request request) {
    this.request = request;
  }

}