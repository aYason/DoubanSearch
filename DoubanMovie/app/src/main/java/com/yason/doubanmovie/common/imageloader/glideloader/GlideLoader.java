package com.yason.doubanmovie.common.imageloader.glideloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.yason.doubanmovie.common.imageloader.ILoaderProxy;
import com.yason.doubanmovie.common.imageloader.LoaderOption;
import com.yason.doubanmovie.common.imageloader.LoaderRequest;
import com.yason.doubanmovie.common.imageloader.callback.ILoadingComplete;
import com.yason.doubanmovie.common.imageloader.callback.ILoadingFailed;
import com.yason.doubanmovie.common.imageloader.callback.ILoadingStarted;
import java.util.concurrent.ExecutionException;


/**
 *
 * Created by yasonxu on 2018/7/16.
 */
public class GlideLoader implements ILoaderProxy {

    @Override
    public void init(Context context) {
    }

    @Override
    public void cancelDisplayTask(ImageView targetView) {
//        GlideApp.with(targetView.getContext()).clear(targetView);
    }

    @Override
    public void clearMemoryCache() {
//        Glide.get(AppRunTime.getInstance().getApplication()).clearMemory();
    }

    @Override
    public void clearDiskCache() {
//        ThreadMgr.postToSubThread(new Runnable() {
//            @Override
//            public void run() {
//                Glide.get(AppRunTime.getInstance().getApplication()).clearDiskCache();
//            }
//        });
    }

    @Override
    public void loadImage(final LoaderRequest request) {
        //Glide必须在主线程调用into()
        if (!currentInMainThread()) {
            throw new IllegalStateException("loadImage() must be call in ui thread!");
        }
        loadImageInternal(request);
    }

    private boolean currentInMainThread() {
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }

    private void loadImageInternal(final LoaderRequest request) {
        Context context = getContext(request.getTargetView());
        GlideRequest<Bitmap> glide = getGlide(context, request.getUri());
        LoaderOption options = request.getOptions();

        if (options.shouldShowImageForEmptyUri()) {
            glide = glide.fallback(options.getImageResForEmptyUri());
        }
        if (options.shouldShowImageOnLoading()) {
            glide = glide.placeholder(options.getImageResOnLoading());
        }
        if (options.shouldShowImageOnFail()) {
            glide = glide.error(options.getImageResOnFail());
        }

        glide = glide.skipMemoryCache(!options.isCacheInMemory());
        if (!options.isCacheOnDisk()) {
            glide = glide.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        } else {
            glide = glide.diskCacheStrategy(DiskCacheStrategy.NONE);
        }

        if (options.getCrossFade() > 0) {
            glide = glide.transition(BitmapTransitionOptions.withCrossFade(options.getCrossFade()));
        }

        //callback
        glide = glide.listener(new RequestListener<Bitmap>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                ILoadingFailed loadingFailed = request.getLoadingFailed();
                if (loadingFailed != null) {
                    loadingFailed.onLoadingFailed(request.getUri(), request.getTargetView(), e);
                }
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                ILoadingComplete loadingComplete = request.getLoadingComplete();
                if (loadingComplete != null) {
                    loadingComplete.onLoadingComplete(request.getUri(), request.getTargetView(), resource);
                }
                return false;
            }
        });

        ILoadingStarted loadingStarted = request.getLoadingStarted();
        if (loadingStarted != null) {
            loadingStarted.onLoadingStart(request.getUri(), request.getTargetView());
        }

        glide.into(request.getTargetView());
    }

    private Context getContext(ImageView targetView) {
        return targetView.getContext();
    }

    private GlideRequest<Bitmap> getGlide(Context context, String uri) {
        return GlideApp
                .with(context)
                .asBitmap()
                .load(uri);
    }

}

