package com.yason.doubanmovie.common.net.cache;

import java.io.Serializable;

/**
 * @author Yason
 * @since 2018/3/6
 */

public class CacheItem implements Serializable {

  private final String mKey;  //存储的key
  private final String mData;       //JSON数据
  private final long mTimeStamp;    //过期时间戳

  private static final long serialVersionUID = 1L;

  public CacheItem(String key, String data, int expire) {
    this.mKey = key;
    this.mData = data;
    this.mTimeStamp = System.currentTimeMillis() + expire * 1000;
  }

  public String getKey() {
    return mKey;
  }

  public String getData() {
    return mData;
  }

  public long getTimeStamp() {
    return mTimeStamp;
  }
}
