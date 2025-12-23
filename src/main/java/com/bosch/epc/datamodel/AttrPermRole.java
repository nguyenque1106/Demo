package com.bosch.epc.datamodel;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * The persistent class for the attr_perm_role database table.
 */
@Entity
@Table(name = "attr_perm_role")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AttrPermRole implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attr_perm_wrkflw_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private AttrPermWorkflow attrPermWorkflow;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pa_role_id", referencedColumnName = "id", nullable = false)
    private ELMRole paRole;

    @ManyToOne(fetch = FetchType.EAGER) // <-- make Permission eager
    @JoinColumn(name = "permission_id", referencedColumnName = "id", nullable = false)
    private ELMPermissions permission;
    

  /**
   * Constructor
   */
  public AttrPermRole() {}

  public AttrPermRole(final ELMRole paRole, final ELMPermissions permission) {
    this.paRole = paRole;
    this.permission = permission;
  }

  public int getId() {
    return this.id;
  }

  public void setId(final int id) {
    this.id = id;
  }

  public AttrPermWorkflow getAttrPermWorkflow() {
    return this.attrPermWorkflow;
  }

  public void setAttrPermWorkflow(AttrPermWorkflow workflow) {
    this.attrPermWorkflow = workflow;
    if (workflow != null && !workflow.getAttrPermRoles().contains(this)) {
        workflow.getAttrPermRoles().add(this);
    }
}

  public ELMRole getPaRole() {
    return this.paRole;
  }

  public void setPaRole(final ELMRole paRole) {
    this.paRole = paRole;
  }

  public ELMPermissions getPermission() {
    return this.permission;
  }

  public void setPermission(final ELMPermissions permission) {
    this.permission = permission;
  }

}