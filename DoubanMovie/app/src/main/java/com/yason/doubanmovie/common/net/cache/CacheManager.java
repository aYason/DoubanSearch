package com.yason.doubanmovie.common.net.cache;


import android.content.Context;
import android.os.Environment;
import android.util.LruCache;
import com.yason.doubanmovie.common.net.cache.DiskLruCache.Editor;
import com.yason.doubanmovie.common.net.cache.DiskLruCache.Snapshot;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * 二级缓存
 *
 * @author Yason
 * @since 2018/3/6
 */

public final class CacheManager {

  private static final int DISK_CACHE_SIZE = 1024 * 1024 * 50;  //50M
  private static final int MEMORY_CACHE_SIZE = (int) (Runtime.getRuntime().maxMemory() / 1024 / 8);
  private DiskLruCache mDiskLruCache;
  private MemoryLruCache mMemoryLruCache;

  public String getFromCache(String url) {
    checkInit();
    final String key = MD5.getMd5(url);
    CacheItem cacheItem = getFromMemoryCache(key);
    if (cacheItem != null) {
      if (System.currentTimeMillis() > cacheItem.getTimeStamp()) {
//        removeFromMemoryCache(key);
        return null;
      } else {
        return cacheItem.getData();
      }
    } else {
      cacheItem = getFromDiskCache(key);
      if (cacheItem != null) {
        if (System.currentTimeMillis() > cacheItem.getTimeStamp()) {
//          removeFromDiskCache(key);
          return null;
        } else {
          mMemoryLruCache.put(key, cacheItem);
          return cacheItem.getData();
        }
      } else {
        return null;
      }
    }
  }

  private boolean removeFromDiskCache(String key) {
    boolean isSuccess = false;
    try {
      isSuccess = mDiskLruCache.remove(key);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return isSuccess;
  }

  private void removeFromMemoryCache(String key) {
    mMemoryLruCache.remove(key);
  }

  private CacheItem getFromMemoryCache(String key) {
    return mMemoryLruCache.get(key);
  }

  private CacheItem getFromDiskCache(String key) {
    final Snapshot snapshot = getSnapshot(key);
    if (snapshot == null) {
      return null;
    } else {
      final InputStream is = snapshot.getInputStream(0);
      ObjectInputStream ois = null;
      try {
        ois = new ObjectInputStream(is);
        return (CacheItem) ois.readObject();
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        try {
          if (ois != null) {
            ois.close();
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      return null;
    }
  }

  public boolean putIntoCache(String url, String value, int expire) {
    if (expire <= 0) {
      return false;
    }
    final String key = MD5.getMd5(url);
    final CacheItem cacheItem = new CacheItem(key, value, expire);

    final boolean isSuccess = putIntoDiskCache(cacheItem);
    if (isSuccess) {
      putIntoMemoryCache(cacheItem);
    }

    return isSuccess;
  }

  private void putIntoMemoryCache(CacheItem cacheItem) {
    mMemoryLruCache.put(cacheItem.getKey(), cacheItem);
  }

  private boolean putIntoDiskCache(CacheItem cacheItem) {
    final Editor editor = getEditor(cacheItem.getKey());
    ObjectOutputStream oos = null;
    boolean isSuccess = false;
    if (editor != null) {
      try {
        final OutputStream os = editor.newOutputStream(0);
        oos = new ObjectOutputStream(os);
        oos.writeObject(cacheItem);
        oos.flush();
        editor.commit();
        isSuccess = true;
      } catch (IOException e) {
        e.printStackTrace();
        try {
          editor.abort();
        } catch (IOException e1) {
          e1.printStackTrace();
        }
      } finally {
        try {
          if (oos != null) {
            oos.close();
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return isSuccess;
  }

  private Snapshot getSnapshot(String key) {
    try {
      return mDiskLruCache.get(key);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  private Editor getEditor(String key) {
    try {
      return mDiskLruCache.edit(key);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  private void checkInit() {
    if (mDiskLruCache == null || mDiskLruCache.isClosed()) {
      throw new IllegalStateException("call init()!");
    }
  }

  public long getCacheSize() {
    return mDiskLruCache.size();
  }

  public boolean clearCache() {
    try {
      mDiskLruCache.delete();
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    mMemoryLruCache.evictAll();
    return true;
  }

  public boolean close() {
    try {
      mDiskLruCache.close();
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    mMemoryLruCache = null;
    mDiskLruCache = null;
    return true;
  }

  public void init(Context context) {
    File cacheDir = createDiskCacheDir(context, "cache");
    try {
      mDiskLruCache = DiskLruCache.open(cacheDir, 1, 1, DISK_CACHE_SIZE);
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }
    mMemoryLruCache = new MemoryLruCache(MEMORY_CACHE_SIZE);
  }

  /**
   * 获取缓存的路径，两个路径在卸载程序时都会删除
   * 有SD卡时获取  /sdcard/Android/data/<application package>/cache
   * 无SD卡时获取  /data/data/<application package>/cache
   */
  private File createDiskCacheDir(Context context, String uniqueName) {
    String cachePath;
    if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
        || !Environment.isExternalStorageRemovable()) {
      cachePath = context.getExternalCacheDir().getPath();
    } else {
      cachePath = context.getCacheDir().getPath();
    }
    File cacheDir = new File(cachePath + File.separator + uniqueName);
    if (!cacheDir.exists()) {
      cacheDir.mkdirs();
    }
    return cacheDir;
  }


  private static class MemoryLruCache extends LruCache<String, CacheItem> {

    /**
     * @param maxSize for caches that do not override {@link #sizeOf}, this is the maximum number of
     * entries in the cache. For all other caches, this is the maximum sum of the sizes of the
     * entries in this cache.
     */
    public MemoryLruCache(int maxSize) {
      super(maxSize);
    }

    @Override
    protected int sizeOf(String key, CacheItem value) {
      //粗略计算一个字符串占内存大小
      return value.getData().length() * 2;
    }
  }

  private CacheManager() {
  }

  public static CacheManager getInstance() {
    return Holder.INSTANCE;
  }

  private static final class Holder {

    private static final CacheManager INSTANCE = new CacheManager();
  }

}
