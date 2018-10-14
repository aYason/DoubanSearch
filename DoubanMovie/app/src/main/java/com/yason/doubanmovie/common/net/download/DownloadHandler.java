package com.yason.doubanmovie.common.net.download;

import android.os.AsyncTask;
import com.yason.doubanmovie.common.net.RestHolder;
import com.yason.doubanmovie.common.net.callback.IError;
import com.yason.doubanmovie.common.net.callback.IFailure;
import com.yason.doubanmovie.common.net.callback.IRequest;
import com.yason.doubanmovie.common.net.callback.ISuccess;
import java.util.WeakHashMap;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Yason
 * @since 2018/1/30
 */

public final class DownloadHandler {

  private final String URL;
  private final WeakHashMap<String, Object> PARAMS;

  private final String DOWNLOAD_DIR;
  private final String NAME;
  private final String EXTENSION;

  private final ISuccess SUCCESS;
  private final IFailure FAILURE;
  private final IError ERROR;
  private final IRequest REQUEST;

  public DownloadHandler(String url,
      WeakHashMap<String, Object> params, String downloadDir,
      String name, String extension,
      ISuccess success, IFailure failure, IError error,
      IRequest request) {
    URL = url;
    PARAMS = params;
    DOWNLOAD_DIR = downloadDir;
    NAME = name;
    EXTENSION = extension;
    SUCCESS = success;
    FAILURE = failure;
    ERROR = error;
    REQUEST = request;
  }

  public void handleDownload() {
    if (REQUEST != null) {
      REQUEST.onRequestStart();
    }

    RestHolder
        .getService()
        .download(URL, PARAMS)
        .enqueue(new Callback<ResponseBody>() {
          @Override
          public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            if (response.isSuccessful()) {
              final ResponseBody body = response.body();
              final SaveFileTask task = new SaveFileTask(SUCCESS, REQUEST);

              task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                  body.byteStream(), DOWNLOAD_DIR, NAME, EXTENSION);

              if (task.isCancelled()) {
                if (REQUEST != null) {
                  REQUEST.onRequestEnd();
                }
              }

            } else {
              if (ERROR != null) {
                ERROR.onError(response.code(), response.message());
              }
              if (REQUEST != null) {
                REQUEST.onRequestEnd();
              }
            }
          }

          @Override
          public void onFailure(Call<ResponseBody> call, Throwable t) {
            if (FAILURE != null) {
              FAILURE.onFailure(t);
            }
            if (REQUEST != null) {
              REQUEST.onRequestEnd();
            }
          }
        });
  }

}
