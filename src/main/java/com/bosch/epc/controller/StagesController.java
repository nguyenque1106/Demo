package com.bosch.epc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bosch.epc.service.StagesServiceImpl;

/**
 * @author VFE1COB Handling request related to stages roles
 */
@RestController
@RequestMapping(value = "/stagesrole")
public class StagesController {

  @Autowired
  StagesServiceImpl service;


  /**
   * Get all stages role from DB
   * 
   * @return list of StagesRole
   */
  @GetMapping(value = "/getall")

  public @ResponseBody Iterable<com.bosch.epc.datamodel.StagesRole> getAllRoles() {
    return service.findAll();
  }

}