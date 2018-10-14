package com.yason.doubanmovie.commonview;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.yason.doubanmovie.R;

/**
 * @author Yason
 * @since 2018/10/10
 */

public class SearchBar extends LinearLayout {

  public interface ISearchListener {
    void onSearchTextChange(String newText);
    void onSearchTextSubmit(String query);
  }

  private TextView mCancelBtn;
  private SearchView mSearchView;

  private ISearchListener mISearchListener;

  public SearchBar(Context context,
      @Nullable AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public SearchBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    setOrientation(HORIZONTAL);

    LayoutInflater.from(getContext()).inflate(R.layout.view_search_bar, this);
    mCancelBtn = findViewById(R.id.cancel);
    mSearchView = findViewById(R.id.searchview);

    mCancelBtn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Context context = v.getContext();
        if (context instanceof Activity) {
          Activity activity = (Activity) context;
          activity.finish();
        }
      }
    });

    mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        if (mISearchListener != null) {
          mISearchListener.onSearchTextSubmit(query);
        }
        return false;
      }

      @Override
      public boolean onQueryTextChange(String newText) {
        if (mISearchListener != null) {
          mISearchListener.onSearchTextChange(newText);
        }
        return false;
      }
    });

    //删除下划线
    mSearchView.findViewById(android.support.v7.appcompat.R.id.search_plate).setBackground(null);
    mSearchView.findViewById(android.support.v7.appcompat.R.id.submit_area).setBackground(null);
  }

  public void registerSearchEvent(ISearchListener searchListener) {
    mISearchListener = searchListener;
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    mISearchListener = null;
  }
}
