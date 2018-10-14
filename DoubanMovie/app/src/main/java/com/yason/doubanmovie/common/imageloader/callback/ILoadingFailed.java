package com.yason.doubanmovie.common.imageloader.callback;

import android.view.View;


/**
 * Created by yasonxu on 2018/7/16.
 */
public interface ILoadingFailed {
    void onLoadingFailed(String imageUri, View view, Exception e);
}
