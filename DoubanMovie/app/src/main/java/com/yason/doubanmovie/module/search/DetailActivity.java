package com.yason.doubanmovie.module.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.alibaba.fastjson.JSON;
import com.yason.doubanmovie.R;
import com.yason.doubanmovie.common.loader.LoaderStyle;
import com.yason.doubanmovie.common.net.HttpMethod;
import com.yason.doubanmovie.common.net.RestClient;
import com.yason.doubanmovie.common.net.callback.IError;
import com.yason.doubanmovie.common.net.callback.IFailure;
import com.yason.doubanmovie.common.net.callback.ISuccess;
import com.yason.doubanmovie.common.util.log.LoggerUtil;
import com.yason.doubanmovie.module.base.BaseActivity;
import com.yason.doubanmovie.module.search.models.MovieItem;
import com.yason.doubanmovie.module.search.views.MovieContentView;

/**
 * @author Yason
 * @since 2018/10/8
 */

public class DetailActivity extends BaseActivity {

  public static final String TAG = "DetailActivity";
  public static final String MOVIE_MD5_URL_ID = "MOVIE_MD5_URL_ID";

  private MovieContentView mMovieContentView;
  private String mMovieMd5UrlId;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);

    mMovieMd5UrlId = getIntent().getStringExtra(MOVIE_MD5_URL_ID);

    initViews();
  }

  private void initViews() {
    mMovieContentView = findViewById(R.id.movie_content_view);
  }

  @Override
  protected void onResume() {
    super.onResume();

    RestClient
        .builder()
        .url("/movie/content/?id=" + mMovieMd5UrlId)
        .method(HttpMethod.GET)
        .success(new ISuccess() {
          @Override
          public void onSuccess(String response) {
            MovieItem movieItem = JSON.parseObject(response, MovieItem.class);
            mMovieContentView.setMovieItem(movieItem);
          }
        })
        .failure(new IFailure() {
          @Override
          public void onFailure(Throwable t) {
            LoggerUtil.e(TAG, "request failed");
            t.printStackTrace();
          }
        })
        .error(new IError() {
          @Override
          public void onError(int code, String msg) {
            LoggerUtil.e(TAG, String.format("code:%d, msg:%s", code, msg));
          }
        })
        .build()
        .execute();
  }

  public static void start(Context context, String movieMd5UrlId) {
    Intent intent = new Intent(context, DetailActivity.class);
    intent.putExtra(MOVIE_MD5_URL_ID, movieMd5UrlId);
    context.startActivity(intent);
  }

}
