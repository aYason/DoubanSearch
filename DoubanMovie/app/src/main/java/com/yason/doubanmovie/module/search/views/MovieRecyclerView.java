package com.yason.doubanmovie.module.search.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.yason.doubanmovie.R;
import com.yason.doubanmovie.common.imageloader.ImageLoader;
import com.yason.doubanmovie.module.search.DetailActivity;
import com.yason.doubanmovie.module.search.models.MovieItem;
import java.util.List;

/**
 * @author Yason
 * @since 2018/10/10
 */

public class MovieRecyclerView extends RecyclerView {

  public static final String TAG = "MovieRecyclerView";
  private Adapter mAdapter;

  public MovieRecyclerView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public MovieRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
  }

  private void init() {
    setLayoutManager(new LinearLayoutManager(getContext()));
    setItemAnimator(new DefaultItemAnimator());
    addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));
  }

  public void setMovies(List<MovieItem> list) {
    mAdapter = new Adapter(list);
    setAdapter(mAdapter);
  }

  private static class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private List<MovieItem> list;

    Adapter(List<MovieItem> list) {
      this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      LayoutInflater inflater = LayoutInflater.from(parent.getContext());
      View itemView = inflater.inflate(R.layout.view_movie_item, parent, false);
      return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
      final MovieItem movieItem = list.get(position);

      holder.mMovieName.setText(movieItem.getName());
      holder.mMovieInfo.setText(getMovieInfo(movieItem));

      ImageLoader
          .getInstance()
          .load(movieItem.getFrontImageUrl())
          .display(holder.mFrontImageView);

      holder.itemView.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          DetailActivity.start(v.getContext(), movieItem.getMd5UrlId());
        }
      });

      Log.e(TAG, movieItem.toString());
    }

    private String getMovieInfo(MovieItem movieItem) {
      // star/type/release_date
      String star = movieItem.getStar();
      String type = join(movieItem.getType().split("/"));
      String releaseDate = movieItem.getReleaseDate().split("/")[0];

      return String.format("%s / %s / %s", star, type, releaseDate);
    }

    private String join(String[] elements) {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < elements.length; i++) {
        sb.append(elements[i]);
        if (i != elements.length - 1) {
          sb.append(" ");
        }
      }
      return sb.toString();
    }


    @Override
    public int getItemCount() {
      return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

      private ImageView mFrontImageView;
      private TextView mMovieName;
      private TextView mMovieInfo;

      ViewHolder(View itemView) {
        super(itemView);
        mFrontImageView = itemView.findViewById(R.id.front_image_view);
        mMovieName = itemView.findViewById(R.id.name);
        mMovieInfo = itemView.findViewById(R.id.info);
      }
    }

  }

}
