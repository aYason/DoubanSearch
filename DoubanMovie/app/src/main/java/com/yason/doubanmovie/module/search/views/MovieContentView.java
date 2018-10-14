package com.yason.doubanmovie.module.search.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.yason.doubanmovie.R;
import com.yason.doubanmovie.common.imageloader.ImageLoader;
import com.yason.doubanmovie.commonview.ExpandableTextView;
import com.yason.doubanmovie.module.search.models.MovieItem;

/**
 * @author Yason
 * @since 2018/10/8
 */

public class MovieContentView extends LinearLayout {

  private ImageView mFrontImageView;
  private TextView mMovieName;
  private TextView mMovieType;
  private TextView mReleaseDate;
  private TextView mLength;
  private MovieScoreView mMovieScoreView;
  private ExpandableTextView mDescriptionText;

  public MovieContentView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public MovieContentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    setOrientation(VERTICAL);

    LayoutInflater.from(getContext()).inflate(R.layout.view_movie_content, this);
    mFrontImageView = findViewById(R.id.front_image_view);
    mMovieName = findViewById(R.id.name);
    mMovieType = findViewById(R.id.type);
    mReleaseDate = findViewById(R.id.release_date);
    mLength = findViewById(R.id.length);
    mMovieScoreView = findViewById(R.id.movie_score_view);
    mDescriptionText = findViewById(R.id.description_text);
  }

  public void setMovieItem(MovieItem movieItem) {
    mMovieName.setText(movieItem.getName());
    mMovieType.setText(movieItem.getType());
    mReleaseDate.setText(movieItem.getReleaseDate());
    mLength.setText(movieItem.getRuntime());
    mMovieScoreView.setScore(movieItem.getStar(), movieItem.getEvaluation());
    mDescriptionText.setContent(movieItem.getDescription());

    ImageLoader
        .getInstance()
        .load(movieItem.getFrontImageUrl())
        .display(mFrontImageView);
  }

}
