package com.yason.doubanmovie.common.net;

import android.content.Context;
import com.yason.doubanmovie.common.loader.Loader;
import com.yason.doubanmovie.common.loader.LoaderStyle;
import com.yason.doubanmovie.common.net.cache.CacheManager;
import com.yason.doubanmovie.common.net.callback.ICookieExpire;
import com.yason.doubanmovie.common.net.callback.IError;
import com.yason.doubanmovie.common.net.callback.IFailure;
import com.yason.doubanmovie.common.net.callback.IRequest;
import com.yason.doubanmovie.common.net.callback.ISuccess;
import com.yason.doubanmovie.common.net.callback.RequestCallback;
import com.yason.doubanmovie.common.net.download.DownloadHandler;
import java.io.File;
import java.util.WeakHashMap;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * @author Yason
 * @since 2018/1/29
 */

public final class RestClient {

  private final String URL;
  private final WeakHashMap<String, Object> PARAMS;
  private final HttpMethod METHOD;
  private final RequestBody BODY;

  private final File FILE;

  private final String DOWNLOAD_DIR;
  private final String NAME;
  private final String EXTENSION;

  private final ISuccess SUCCESS;
  private final IFailure FAILURE;
  private final IError ERROR;
  private final IRequest REQUEST;
  private final ICookieExpire COOKIE_EXPIRE;

  private final LoaderStyle LOADER_STYLE;
  private final Context CONTEXT;

  private final int EXPIRE;
  private final boolean FORCE_UPDATE;

  RestClient(
      String url,
      WeakHashMap<String, Object> params,
      HttpMethod method,
      RequestBody body,
      File file,
      String downloadDir,
      String name,
      String extension,
      ISuccess success,
      IFailure failure,
      IError error,
      IRequest request,
      ICookieExpire cookieExpire, LoaderStyle loaderStyle,
      Context context,
      int expire,
      boolean forceUpdate) {
    URL = url;
    PARAMS = params;
    METHOD = method;
    BODY = body;
    FILE = file;
    DOWNLOAD_DIR = downloadDir;
    NAME = name;
    EXTENSION = extension;
    SUCCESS = success;
    FAILURE = failure;
    ERROR = error;
    REQUEST = request;
    COOKIE_EXPIRE = cookieExpire;
    LOADER_STYLE = loaderStyle;
    CONTEXT = context;
    EXPIRE = expire;
    FORCE_UPDATE = forceUpdate;
  }

  public static RestClientBuilder builder() {
    return new RestClientBuilder();
  }

  public void execute() {
    if (METHOD == HttpMethod.DOWNLOAD || METHOD == HttpMethod.UPLOAD) {
      getResponseFromNet();
    } else {
      if (!FORCE_UPDATE && EXPIRE > 0) {
        String response = getResponseFromCache();
        if (response != null) {
          if (SUCCESS != null) {
            SUCCESS.onSuccess(response);
          }
        } else {
          getResponseFromNet();
        }
      } else {
        getResponseFromNet();
      }
    }
  }

  private void handleDownload() {
    new DownloadHandler(
        URL,
        PARAMS,
        DOWNLOAD_DIR,
        NAME, EXTENSION,
        SUCCESS,
        FAILURE,
        ERROR,
        REQUEST)
        .handleDownload();
  }

  private String getResponseFromCache() {
    return CacheManager.getInstance().getFromCache(URL);
  }

  private void getResponseFromNet() {
    if (METHOD == HttpMethod.DOWNLOAD) {
      handleDownload();
      return;
    }

    final RestService restService = RestHolder.getService();
    Call<String> call = null;

    if (REQUEST != null) {
      REQUEST.onRequestStart();
    }

    if (LOADER_STYLE != null) {
      Loader.showLoading(CONTEXT, LOADER_STYLE);
    }

    switch (METHOD) {
      case GET:
        call = restService.get(URL, PARAMS);
        break;
      case POST:
        call = restService.post(URL, PARAMS);
        break;
      case POST_RAW:
        call = restService.postRaw(URL, BODY);
        break;
      case UPLOAD:
        final RequestBody body =
            RequestBody.create(MediaType.parse("multipart/form-data"), FILE);
        final MultipartBody.Part part =
            MultipartBody.Part.createFormData("file", FILE.getName(), body);
        call = restService.upload(URL, part);
        break;
      default:
        break;
    }

    if (call != null) {
      call.enqueue(getCallback());
    }
  }

  private Callback<String> getCallback() {
    return new RequestCallback(
        URL,
        SUCCESS,
        FAILURE,
        ERROR,
        REQUEST,
        COOKIE_EXPIRE,
        LOADER_STYLE,
        EXPIRE);
  }

}
