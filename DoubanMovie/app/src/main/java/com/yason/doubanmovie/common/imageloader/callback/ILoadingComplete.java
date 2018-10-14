package com.yason.doubanmovie.common.imageloader.callback;

import android.graphics.Bitmap;
import android.view.View;

/**
 * Created by yasonxu on 2018/7/16.
 */
public interface ILoadingComplete {
    void onLoadingComplete(String imageUri, View view, Bitmap loadedImage);
}
