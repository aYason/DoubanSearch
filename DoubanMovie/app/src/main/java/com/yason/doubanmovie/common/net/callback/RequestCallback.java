package com.yason.doubanmovie.common.net.callback;

import com.yason.doubanmovie.common.loader.Loader;
import com.yason.doubanmovie.common.loader.LoaderStyle;
import com.yason.doubanmovie.common.net.cache.CacheManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Yason
 * @since 2018/1/29
 */

public final class RequestCallback implements Callback<String> {

  private final String URL;
  private final ISuccess SUCCESS;
  private final IFailure FAILURE;
  private final IError ERROR;
  private final IRequest REQUEST;
  private final ICookieExpire COOKIE_EXIPRE;
  private final LoaderStyle LOADER_STYLE;
  private final int EXPIRE;
//  private static final Handler HANDLER = new Handler();

  public RequestCallback(
      String url,
      ISuccess success,
      IFailure failure,
      IError error,
      IRequest request,
      ICookieExpire cookieExipre, LoaderStyle loaderStyle,
      int expire) {
    URL = url;
    SUCCESS = success;
    FAILURE = failure;
    ERROR = error;
    REQUEST = request;
    COOKIE_EXIPRE = cookieExipre;
    LOADER_STYLE = loaderStyle;
    EXPIRE = expire;
  }

  @Override
  public void onResponse(Call<String> call, Response<String> response) {
    if (response.isSuccessful()) {
      if (SUCCESS != null) {
        String value = response.body();
        if (EXPIRE > 0) {
          CacheManager.getInstance().putIntoCache(URL, value, EXPIRE);
        }
        SUCCESS.onSuccess(value);
      }
    } else {
      if (ERROR != null) {
        ERROR.onError(response.code(), response.message());
      }
    }
    if (REQUEST != null) {
      REQUEST.onRequestEnd();
    }

    onRequestFinish();
  }

  @Override
  public void onFailure(Call call, Throwable t) {
    if (FAILURE != null) {
      FAILURE.onFailure(t);
    }
    if (REQUEST != null) {
      REQUEST.onRequestEnd();
    }

    onRequestFinish();
  }

  private void onRequestFinish() {
    if (LOADER_STYLE != null) {
//      HANDLER.postDelayed(new Runnable() {
//        @Override
//        public void run() {
//          Loader.stopLoading();
//        }
//      }, 1000);

      Loader.stopLoading();

    }

  }

}
