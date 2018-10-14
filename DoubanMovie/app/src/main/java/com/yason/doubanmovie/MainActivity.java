package com.yason.doubanmovie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SearchView;
import com.joanzapata.iconify.widget.IconTextView;
import com.yason.doubanmovie.module.base.BaseActivity;
import com.yason.doubanmovie.module.search.SearchActivity;

public class MainActivity extends BaseActivity {

  private IconTextView mSearchBtn;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initViews();
  }

  private void initViews() {
    mSearchBtn = findViewById(R.id.search_btn);
    mSearchBtn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        startActivity(intent);
      }
    });
  }


}
