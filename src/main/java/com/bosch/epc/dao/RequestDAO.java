/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import com.bosch.epc.datamodel.WORequest;
import com.bosch.epc.model.WorkOnResponse;
import com.bosch.epc.model.WorkOnStatusReponse;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author VFE1COB DAO to handle all the epc requests, workon request Interacts with DB and stores and fetches necessary
 *         data
 */
@Repository
@Transactional
public class RequestDAO {

  private static final Logger logger = LoggerFactory.getLogger(RequestDAO.class);
  @Value("${createRequestURL}")
  private String createRequestURL;
  @Value("${keyId}")
  private String keyId;
  @Value("${host}")
  private String host;
  @Value("${contentType}")
  private String contentType;
  @Value("${statusURL}")
  private String statusRequestURL;
  @Value("${status}")
  private String status;
  @Value("${workflowLink}")
  private String workflowLink;
  @Value("${locale}")
  private String locale;

  @Autowired
  WorkONRepository repo;

  /**
   * Get Request current status
   *
   * @param id request id
   * @return the current status of the request
   * @throws IOException 
   * @throws Exception Any exception while invoking workon
   */
  public String getRequestStatus(final String id) throws IOException{

    HttpURLConnection con = null;
    URL obj = new URL(this.statusRequestURL + id);
      con = (HttpURLConnection) obj.openConnection();
      con.setRequestMethod("GET");
      con.setRequestProperty("requestKey", id);
      // Add request headers if necessary
      con.setRequestProperty("KeyId", this.keyId);
      con.setRequestProperty("Host", this.host);
      int responseCode = con.getResponseCode();
      logger.info("Response Code:{}", responseCode);

      // If response code is 200 (HTTP OK)
      if (responseCode == HttpURLConnection.HTTP_OK) {
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
          response.append(inputLine);
        }
        in.close();
        ObjectMapper objectMapper = new ObjectMapper();
        WorkOnStatusReponse jsonResponse = objectMapper.readValue(response.toString(), WorkOnStatusReponse.class);
        logger.info(jsonResponse.getRequestKey() + " corresponding resolution status" + jsonResponse.getResolution());
       return jsonResponse.getResolution();
      }
      logger.error("Failure response for status retrieval {}", con.getResponseCode());

    return null;
  }

  /**
   * Create new EPC request
   *
   * @param jsonString jsonstring from the request object
   * @return status of request Id
   * @throws IOException 
   */
  public WORequest createEPCRequest(final String jsonString) throws IOException {
    HttpURLConnection conn = null;
      URL url = new URL(this.createRequestURL);
      conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("PUT");
      conn.setRequestProperty("Content-Type", this.contentType);
      conn.setRequestProperty("KeyId", this.keyId);
      conn.setRequestProperty("Host", this.host);
      conn.setDoOutput(true);

      logger.debug("Create request{}", jsonString);
      // sending requestBody

      try (OutputStream os = conn.getOutputStream()) {
        byte[] input = jsonString.getBytes("utf-8");
        os.write(input, 0, input.length);
      }
      int responseCode = conn.getResponseCode();
      logger.info("Create request response code{}", responseCode);
      conn.getResponseMessage();
      // If response code is 200 (HTTP OK)
      if (responseCode == HttpURLConnection.HTTP_OK) {
        // Read the response
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
          response.append(inputLine);
        }
        in.close();
        ObjectMapper objectMapper = new ObjectMapper();
        WorkOnResponse jsonResponse = objectMapper.readValue(response.toString(), WorkOnResponse.class);
        logger.debug("Created request number{}", jsonResponse.getKey());
        WORequest woRequest = new WORequest();
        woRequest.setWorkonId(jsonResponse.getKey());
        woRequest.setStatus(this.status);
        return woRequest;
      }

    return null;
  }


  /**
   * @param woRequest save the woRequest details in DB
   */
  public void add(@RequestBody final WORequest woRequest) {
    this.repo.save(woRequest);
  }


  /**
   * Used for test case purpose
   *
   * @param host
   */
  public void setHost(final String host) {
    this.host = host;

  }

  /**
   * @param content
   */
  public void setContentType(final String content) {
    this.contentType = content;

  }

  /**
   * @param createRequestURL
   */
  public void setCreateRequestURL(final String createRequestURL) {
    this.createRequestURL = createRequestURL;

  }

  /**
   * @param statusURL
   */
  public void setStatusURL(final String statusURL) {
    this.statusRequestURL = statusURL;

  }

  /**
   * @param keyId
   */
  public void setKeyId(final String keyId) {
    this.keyId = keyId;

  }

}
