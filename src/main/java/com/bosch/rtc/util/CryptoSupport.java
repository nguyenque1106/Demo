package com.bosch.rtc.util;

import java.io.UnsupportedEncodingException;

/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */

/**
 * CryptoSupport Class to decrypt the User Name and Password
 * 
 * @author PPT4KOR
 */
public class CryptoSupport {

  private static String cryped = "_C+!T=igk.*FGe$$Cvp@^'" + "zoRf2qrw/n8m mZD''TmNY%!BTLVv&,am ic=" +
      "XL=Vm6#i[4Mw7x7#-KSxX[Zh&!_4E`pnns[T/" + "quW;2cyjEB_=b3j]8fWGk-.,5,q3o$^r.t:Z^" +
      "$g=AbnVAaN}P]}z8[9zu5bggRd6Rmo?'^M" + "uxs?&!9bpYKa-GU6,T/_p$kqUKs@F/ BN3D}i" +
      "?kvdc&fr'XmW`p6*$V7DLPeqi6bZX&#=xmgk6" + "V{npS2v#+;nS*add23j38/(&543$%&(9=(%(7";

  private static byte[] crypedByte;


  /**
   * constructor
   */
  private CryptoSupport() {
    // Do Nothing
  }


  /**
   * Encrypt the value
   * 
   * @param valueToCrypt : valueToCrypt
   * @return encrypted Value
   * @throws UnsupportedEncodingException : UnsupportedEncodingException
   */
  @SuppressWarnings("cast")
  public static String crypto(String valueToCrypt) throws UnsupportedEncodingException {

    char[] calCryped = new char[256];
    crypedByte = new byte[256];

    crypedByte = cryped.getBytes("UTF8");
    // Key-Creation
    int u = 120;
    for (int index = 0; index < 120; index++) {

      u--;

      int x = ((int) crypedByte[u]) / (int) crypedByte[index];

      x = x ^ index;
      if (index == 0)
        index = 1;

      x = ((x * u) / index) + x;

      if (x > 120)
        x = cryped.toCharArray()[index];

      if (x == 120)
        x = cryped.toCharArray()[u];

      if (x < 40)
        x = cryped.toCharArray()[index + 1];

      x = recursive((byte) x, index);
      calCryped[u] = (char) x;

    }

    char[] crypedChar = calCryped;
    char[] valueToCryptChar = valueToCrypt.toCharArray();
    char[] retChar = new char[valueToCryptChar.length];

    for (int i = 0; i < valueToCryptChar.length; i++)
      retChar[i] = (char) ((int) crypedChar[i] ^ (int) valueToCryptChar[i]);

    return String.copyValueOf(retChar);

  }

  private static byte recursive(byte para, int counter) {
    int nextCounter = counter - 1;
    byte paraUpdated = (byte) (para ^ crypedByte[counter]);

    if (nextCounter == 0) {
      return paraUpdated;
    }
    return recursive(paraUpdated, nextCounter);

  }

}
