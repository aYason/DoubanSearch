package com.yason.doubanmovie.module.search.views;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import com.joanzapata.iconify.widget.IconTextView;
import com.yason.doubanmovie.R;

/**
 * @author Yason
 * @since 2018/10/9
 */

public class TranslucentTitleBar extends FrameLayout {

  private IconTextView mBackView;
  private IconTextView mTitleView;
  private IconTextView mShareView;

  public TranslucentTitleBar(@NonNull Context context,
      @Nullable AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public TranslucentTitleBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    setBackgroundColor(getResources().getColor(android.R.color.transparent));

    LayoutInflater.from(getContext()).inflate(R.layout.view_title_bar, this);
    mBackView = findViewById(R.id.back);
    mTitleView = findViewById(R.id.title);
    mShareView = findViewById(R.id.share);

    mBackView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Context context = v.getContext();
        if (context instanceof Activity) {
          Activity activity = (Activity) context;
          activity.finish();
        }
      }
    });
  }

}
