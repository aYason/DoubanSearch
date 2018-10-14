package com.yason.doubanmovie.common.net.cache;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Yason
 * @since 2018/3/6
 */

public class MD5 {

  /** MD5算法 */
  public static String getMd5(String url) {
    String cacheKey;
    try {
      final MessageDigest mDigest = MessageDigest.getInstance("MD5");
      mDigest.update(url.getBytes());
      cacheKey = ToHexString(mDigest.digest());
    } catch (NoSuchAlgorithmException e) {
      cacheKey = String.valueOf(url.hashCode());
    }
    return cacheKey;
  }

  private static String ToHexString(byte[] bytes) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < bytes.length; i++) {
      String hex = Integer.toHexString(0xFF & bytes[i]);
      if (hex.length() == 1) {
        sb.append(hex);
      }
    }
    return sb.toString();
  }

}
