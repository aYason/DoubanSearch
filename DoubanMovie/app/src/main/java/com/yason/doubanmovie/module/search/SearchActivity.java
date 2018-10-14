package com.yason.doubanmovie.module.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.yason.doubanmovie.R;
import com.yason.doubanmovie.common.net.HttpMethod;
import com.yason.doubanmovie.common.net.RestClient;
import com.yason.doubanmovie.common.net.callback.IError;
import com.yason.doubanmovie.common.net.callback.IFailure;
import com.yason.doubanmovie.common.net.callback.ISuccess;
import com.yason.doubanmovie.common.util.log.LoggerUtil;
import com.yason.doubanmovie.module.base.BaseActivity;
import com.yason.doubanmovie.module.search.models.MovieItem;
import com.yason.doubanmovie.module.search.views.MovieRecyclerView;
import com.yason.doubanmovie.commonview.SearchBar;
import com.yason.doubanmovie.commonview.SearchBar.ISearchListener;
import java.util.List;

/**
 * @author Yason
 * @since 2018/10/8
 */

public class SearchActivity extends BaseActivity {

  private static final String TAG = "SearchActivity";

  private SearchBar mSearchBar;
  private MovieRecyclerView mMovieRecyclerView;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search);

    initViews();
  }

  private void initViews() {
    mSearchBar = findViewById(R.id.searchbar);
    mMovieRecyclerView = findViewById(R.id.recycler_view);

    mSearchBar.registerSearchEvent(new ISearchListener() {
      @Override
      public void onSearchTextChange(String newText) {
        searchMovie(newText);
      }

      @Override
      public void onSearchTextSubmit(String query) {

      }
    });
  }

  private void searchMovie(String newText) {
    RestClient
        .builder()
        .url("/movie/search/?k=" + newText)
        .method(HttpMethod.GET)
        .success(new ISuccess() {
          @Override
          public void onSuccess(String response) {
            List<MovieItem> list = JSON.parseObject(response, new TypeReference<List<MovieItem>>() {});
            mMovieRecyclerView.setMovies(list);
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

  @Override
  protected void onResume() {
    super.onResume();
  }
}
