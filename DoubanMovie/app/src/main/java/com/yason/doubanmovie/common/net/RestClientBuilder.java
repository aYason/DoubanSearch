package com.yason.doubanmovie.common.net;

import android.content.Context;
import com.yason.doubanmovie.common.config.Config;
import com.yason.doubanmovie.common.config.ConfigKeys;
import com.yason.doubanmovie.common.loader.LoaderStyle;
import com.yason.doubanmovie.common.net.callback.ICookieExpire;
import com.yason.doubanmovie.common.net.callback.IError;
import com.yason.doubanmovie.common.net.callback.IFailure;
import com.yason.doubanmovie.common.net.callback.IRequest;
import com.yason.doubanmovie.common.net.callback.ISuccess;
import java.io.File;
import java.util.WeakHashMap;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * @author Yason
 * @since 2018/1/29
 */

public final class RestClientBuilder {

  //params
  private String mUrl;
  private HttpMethod mMethod = HttpMethod.GET;
  private WeakHashMap<String, Object> mParams = new WeakHashMap<>();
  private RequestBody mBody;

  //upload
  private File mFile;

  //download
  private String mDownloadDir;
  private String mName;
  private String mExtension;

  //callback
  private ISuccess mISuccess;
  private IFailure mIFailure;
  private IError mIError;
  private IRequest mIRequest;
  private ICookieExpire mCookieExpire;

  //loader
  private LoaderStyle mLoaderStyle;
  private Context mContext;

  //缓存,下载和上传不缓存
  private int mExpire = 0;
  private boolean mForceUpdate = false;

  RestClientBuilder() {
  }

  public RestClient build() {
    return new RestClient(
        mUrl,
        mParams,
        mMethod,
        mBody,
        mFile,
        mDownloadDir,
        mName,
        mExtension,
        mISuccess,
        mIFailure,
        mIError,
        mIRequest,
        mCookieExpire,
        mLoaderStyle,
        mContext,
        mExpire,
        mForceUpdate);
  }

  public RestClientBuilder url(String url) {
    this.mUrl = url;
    return this;
  }

  public RestClientBuilder params(String key, Object value) {
    mParams.put(key, value);
    return this;
  }

  public final RestClientBuilder params(WeakHashMap<String, Object> params) {
    mParams.putAll(params);
    return this;
  }

  public RestClientBuilder method(HttpMethod method) {
    this.mMethod = method;
    return this;
  }

  public RestClientBuilder raw(String raw) {
    this.mBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), raw);
    return this;
  }

  public RestClientBuilder file(File file) {
    this.mFile = file;
    return this;
  }

  public RestClientBuilder file(String path) {
    this.mFile = new File(path);
    return this;
  }

  public RestClientBuilder success(ISuccess success) {
    this.mISuccess = success;
    return this;
  }

  public RestClientBuilder failure(IFailure failure) {
    this.mIFailure = failure;
    return this;
  }

  public RestClientBuilder error(IError error) {
    this.mIError = error;
    return this;
  }

  public RestClientBuilder request(IRequest request) {
    this.mIRequest = request;
    return this;
  }

  public RestClientBuilder cookieExipre(ICookieExpire cookieExpire) {
    this.mCookieExpire = cookieExpire;
    return this;
  }

  public RestClientBuilder downloadDir(String downloadDir) {
    this.mDownloadDir = downloadDir;
    return this;
  }

  public RestClientBuilder setmName(String name) {
    this.mName = name;
    return this;
  }

  public RestClientBuilder extension(String extension) {
    this.mExtension = extension;
    return this;
  }

  public RestClientBuilder loaderStyle(LoaderStyle loaderStyle) {
    this.mLoaderStyle = loaderStyle;
    this.mContext = Config.getConfiguration(ConfigKeys.ACTIVITY);
    return this;
  }

  public RestClientBuilder expire(int seconds) {
    this.mExpire = seconds;
    return this;
  }

  public RestClientBuilder forceUpdate(boolean forceUpdate) {
    this.mForceUpdate = forceUpdate;
    return this;
  }
}

