package com.bosch.epc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;


/**
 * @author VFE1COB The main class serves two purpose in a spring boot application: Configuration and bootstrapping.
 * @SpringBootApplication This annotation enables the auto-configuration feature of the spring boot module (i.e.
 *                        java-based configuration and component scanning)
 */
@SpringBootApplication
@EnableCaching
@EnableScheduling
@EnableAutoConfiguration
@OpenAPIDefinition(info = @Info(title = "EPC Tool", version = "1.0", description = "Employee Permission Tool To Manage the ELM permissions, Manage the workitem attribute Permissions and sync the Stages roles to ELM Roles."))
public class EPCApplication extends SpringBootServletInitializer {

  /**
   * @param args
   */
  public static void main(final String[] args) {
    System.setProperty("server.servlet.context-path", "/epc");
    SpringApplication.run(EPCApplication.class, args);
  }
}