package com.yason.doubanmovie.module.search.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.yason.doubanmovie.R;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * @author Yason
 * @since 2018/10/8
 */

public class MovieScoreView extends LinearLayout {

  private static final String TAG = "MovieScoreView";

  private TextView mMovieScore;
  private MaterialRatingBar mRatingBar;
  private TextView mMovieEvaluation;

  public MovieScoreView(Context context,
      @Nullable AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public MovieScoreView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    setOrientation(VERTICAL);

    LayoutInflater.from(getContext()).inflate(R.layout.view_movie_score, this);
    mMovieScore = findViewById(R.id.score);
    mRatingBar = findViewById(R.id.ratingbar);
    mMovieEvaluation = findViewById(R.id.evaluation);
  }

  public void setScore(String score, String evaluation) {
    mMovieScore.setText(score);

    final float rating = 0.5f;
    mRatingBar.setRating(Float.valueOf(score) * rating);

    mMovieEvaluation.setText(String.format("%s人", evaluation));
  }


}
