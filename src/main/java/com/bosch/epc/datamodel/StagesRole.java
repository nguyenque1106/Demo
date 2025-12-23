
package com.bosch.epc.datamodel;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * @author VFE1COB
 */
@Entity
@Table(name = "stagesrole")
public class StagesRole {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private String uniqueId;
  private String name;
  private String createdBy;
  private Date creationDate;
  private String modifiedBy;
  private Date modificationDate;


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
   * @return the uniqueId
   */
  public String getUniqueId() {
    return this.uniqueId;
  }

  /**
   * @param uniqueId the uniqueId to set
   */
  public void setUniqueId(final String uniqueId) {
    this.uniqueId = uniqueId;
  }

  /**
   * @return the name
   */
  public String getName() {
    return this.name;
  }

  /**
   * @param name the name to set
   */
  public void setName(final String name) {
    this.name = name;
  }

  /**
   * @return the createdBy
   */
  public String getCreatedBy() {
    return this.createdBy;
  }

  /**
   * @param createdBy the createdBy to set
   */
  public void setCreatedBy(final String createdBy) {
    this.createdBy = createdBy;
  }

  /**
   * @return the creationDate
   */
  public Date getCreationDate() {
    return this.creationDate;
  }

  /**
   * @param creationDate the creationDate to set
   */
  public void setCreationDate(final Date creationDate) {
    this.creationDate = creationDate;
  }

  /**
   * @return the modifiedBy
   */
  public String getModifiedBy() {
    return this.modifiedBy;
  }

  /**
   * @param modifiedBy the modifiedBy to set
   */
  public void setModifiedBy(final String modifiedBy) {
    this.modifiedBy = modifiedBy;
  }

  /**
   * @return the modificationDate
   */
  public Date getModificationDate() {
    return this.modificationDate;
  }

  /**
   * @param modificationDate the modificationDate to set
   */
  public void setModificationDate(final Date modificationDate) {
    this.modificationDate = modificationDate;
  }


}
