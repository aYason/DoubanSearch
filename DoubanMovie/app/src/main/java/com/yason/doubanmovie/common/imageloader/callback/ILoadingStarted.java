package com.yason.doubanmovie.common.imageloader.callback;

import android.view.View;

/**
 * Created by yasonxu on 2018/7/16.
 */
public interface ILoadingStarted {
    void onLoadingStart(String imageUri, View view);
}
