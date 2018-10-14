package com.yason.doubanmovie.common.imageloader;

import android.content.Context;
import android.widget.ImageView;
import com.yason.doubanmovie.common.imageloader.glideloader.GlideLoader;

/**
 *
 * Created by yasonxu on 2018/7/19.
 */
public class ImageLoader {
    private ILoaderProxy mILoaderProxy;
    private boolean isInited = false;

    private ImageLoader() {
    }

    private ILoaderProxy getDefaultLoader() {
        return new GlideLoader();
    }

    public LoaderRequest.Builder load(String uri) {
        return new LoaderRequest.Builder().uri(uri);
    }

    public void init(Context context) {
        if (isInited) {
            throw new IllegalStateException("ImageLoader has been initialized");
        }

        mILoaderProxy = getDefaultLoader();
        mILoaderProxy.init(context);
        isInited = true;
    }

    public void cancelDisplayTask(ImageView targetView) {
        checkisInited();
        mILoaderProxy.cancelDisplayTask(targetView);
    }

    public void clearMemoryCache() {
        checkisInited();
        mILoaderProxy.clearMemoryCache();
    }

    public void clearDiskCache() {
        checkisInited();
        mILoaderProxy.clearDiskCache();
    }

    public boolean isInited() {
        return isInited;
    }
    
    void loadRequest(LoaderRequest request) {
        checkisInited();
        mILoaderProxy.loadImage(request);
    }

    private void checkisInited() {
        if (!isInited) {
            throw new IllegalStateException("please call init()");
        }
    }

    public static ImageLoader getInstance() {
        return Holder.INSTANCE;
    }

    private static final class Holder {
        private static final ImageLoader INSTANCE = new ImageLoader();
    }
}
