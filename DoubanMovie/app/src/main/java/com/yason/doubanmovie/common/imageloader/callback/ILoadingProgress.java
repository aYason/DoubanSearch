package com.yason.doubanmovie.common.imageloader.callback;

import android.view.View;

/**
 * Created by yasonxu on 2018/7/19.
 */
public interface ILoadingProgress {
    void onProgressUpdate(String imageUri, View view, int current, int total);
}
