package com.bosch.epc.datamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
 * The persistent class for the attr_perm_workflow database table.
 */
@Entity
@Table(name = "attr_perm_workflow")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AttrPermWorkflow implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String wiresolution;
    private String wistatus;
    private String wistatusgrp;
    private String witype;

    @OneToMany(mappedBy = "attrPermWorkflow", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<AttrPermRole> attrPermRoles = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conditionid", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private AttrPermCondition attrPermCondition;



  public void addAttrPermRole(AttrPermRole role) {
    attrPermRoles.add(role);
    role.setAttrPermWorkflow(this);
}

public void removeAttrPermRole(AttrPermRole role) {
    attrPermRoles.remove(role);
    role.setAttrPermWorkflow(null);
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

  public String getWiresolution() {
    return this.wiresolution;
  }

  public void setWiresolution(final String wiresolution) {
    this.wiresolution = wiresolution;
  }

  public String getWistatus() {
    return this.wistatus;
  }

  public void setWistatus(final String wistatus) {
    this.wistatus = wistatus;
  }

  public String getWistatusgrp() {
    return this.wistatusgrp;
  }

  public void setWistatusgrp(final String wistatusgrp) {
    this.wistatusgrp = wistatusgrp;
  }

  public String getWitype() {
    return this.witype;
  }

  public void setWitype(final String witype) {
    this.witype = witype;
  }

  public List<AttrPermRole> getAttrPermRoles() {
    return this.attrPermRoles;
  }

  public void setAttrPermRoles(final List<AttrPermRole> attrPermRoles) {
    this.attrPermRoles = attrPermRoles;
  }


  public AttrPermCondition getAttrPermCondition() {
    return this.attrPermCondition;
  }

  public void setAttrPermCondition(final AttrPermCondition attrPermCondition) {
    this.attrPermCondition = attrPermCondition;
  }

}