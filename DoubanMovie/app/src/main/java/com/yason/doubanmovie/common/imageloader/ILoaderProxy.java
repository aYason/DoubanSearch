package com.yason.doubanmovie.common.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;


/**
 * Created by yasonxu on 2018/7/16.
 */
public interface ILoaderProxy {
    void init(Context context);
    void loadImage(LoaderRequest loaderRequest);
    void cancelDisplayTask(ImageView targetView);
    void clearMemoryCache();
    void clearDiskCache();
}
