/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.rtc.util;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;


/**
 * @author GHT9HC
 */
public final class AttributeUtils {

  private AttributeUtils() {}

  /**
   * @param specificationFile
   * @return
   * @throws ParserConfigurationException
   * @throws IOException
   * @throws SAXException
   */
  public static Document parseDocumentByFile(final File specificationFile) {
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

      // Prevent XXE (XML External Entity) attacks
      factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
      factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
      factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
      factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
      factory.setXIncludeAware(false);
      factory.setExpandEntityReferences(false);

      // Create a DocumentBuilderFactory and DocumentBuilder to parse the XML
      DocumentBuilder builder = factory.newDocumentBuilder();

      // Parse the XML file
      Document document = builder.parse(specificationFile);
      document.getDocumentElement().normalize();
      return document;
    }
    catch (Exception e) {
      return null;
    }
  }

  /**
   * @param filePath
   * @return
   */
  public static boolean isFileExist(final String filePath) {
    return new File(filePath).exists();
  }
}
