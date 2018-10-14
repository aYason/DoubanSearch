package com.yason.doubanmovie.common.imageloader.callback;

import android.view.View;

/**
 * Created by yasonxu on 2018/7/19.
 */
public interface ILoadingCancelled {
    void onLoadingCancelled(String imageUri, View view);
}
