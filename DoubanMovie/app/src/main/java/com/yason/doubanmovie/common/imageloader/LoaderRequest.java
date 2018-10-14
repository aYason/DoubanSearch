package com.yason.doubanmovie.common.imageloader;

import android.support.annotation.DrawableRes;
import android.widget.ImageView;
import com.yason.doubanmovie.common.imageloader.callback.ILoadingCancelled;
import com.yason.doubanmovie.common.imageloader.callback.ILoadingComplete;
import com.yason.doubanmovie.common.imageloader.callback.ILoadingFailed;
import com.yason.doubanmovie.common.imageloader.callback.ILoadingProgress;
import com.yason.doubanmovie.common.imageloader.callback.ILoadingStarted;


/**
 * Created by yasonxu on 2018/7/17.
 */
public class LoaderRequest {
    private final String uri;
    private final LoaderOption options;
    private final ImageView targetView;

    //callback
    private final ILoadingStarted loadingStarted;
    private final ILoadingComplete loadingComplete;
    private final ILoadingFailed loadingFailed;
    private final ILoadingCancelled loadingCancelled;
    private final ILoadingProgress loadingProgress;

    LoaderRequest(Builder builder) {
        this.uri = builder.uri;
        this.options = builder.optionBuilder.build();
        this.targetView = builder.targetView;
        this.loadingStarted = builder.loadingStarted;
        this.loadingComplete = builder.loadingComplete;
        this.loadingFailed = builder.loadingFailed;
        this.loadingCancelled = builder.loadingCancelled;
        this.loadingProgress = builder.loadingProgress;
    }

    public String getUri() {
        return this.uri;
    }

    public LoaderOption getOptions() {
        return this.options;
    }

    public ImageView getTargetView() {
        return this.targetView;
    }

    public ILoadingComplete getLoadingComplete() {
        return this.loadingComplete;
    }

    public ILoadingFailed getLoadingFailed() {
        return this.loadingFailed;
    }

    public ILoadingStarted getLoadingStarted() {
        return this.loadingStarted;
    }

    public ILoadingCancelled getLoadingCancelled() {
        return this.loadingCancelled;
    }

    public ILoadingProgress getLoadingProgress() {
        return this.loadingProgress;
    }

    public static class Builder {
        private String uri;
        private LoaderOption.Builder optionBuilder;
        private ImageView targetView;

        //callback
        private ILoadingStarted loadingStarted;
        private ILoadingFailed loadingFailed;
        private ILoadingComplete loadingComplete;
        private ILoadingCancelled loadingCancelled;
        private ILoadingProgress loadingProgress;

        Builder() {
            uri = null;
            optionBuilder = LoaderOption.builder();
            targetView = null;
            loadingStarted = null;
            loadingFailed = null;
            loadingComplete = null;
            loadingCancelled = null;
            loadingProgress = null;
        }

        public Builder uri(String uri) {
            this.uri = uri;
            return this;
        }

        public Builder loadingComplete(ILoadingComplete loadingComplete) {
            this.loadingComplete = loadingComplete;
            return this;
        }

        public Builder loadingStarted(ILoadingStarted loadingStarted) {
            this.loadingStarted = loadingStarted;
            return this;
        }

        public Builder loadingFailed(ILoadingFailed loadingFailed) {
            this.loadingFailed = loadingFailed;
            return this;
        }

        public Builder loadingCancelled(ILoadingCancelled loadingCancelled) {
            this.loadingCancelled = loadingCancelled;
            return this;
        }

        public Builder loadingProgress(ILoadingProgress loadingProgress) {
            this.loadingProgress = loadingProgress;
            return this;
        }

        public Builder apply(LoaderOption options) {
            this.optionBuilder = options.newBuilder();
            return this;
        }

        public void display(ImageView targetView) {
            this.targetView = targetView;
            loadRequest();
        }

        private void loadRequest() {
            if (this.targetView == null) {
                throw new NullPointerException("targetView can not be null!");
            }
            LoaderRequest request = new LoaderRequest(this);
            ImageLoader.getInstance().loadRequest(request);
        }

        public Builder showImageForEmptyUri(@DrawableRes int imageRes) {
            optionBuilder.showImageForEmptyUri(imageRes);
            return this;
        }

        public Builder showImageOnLoading(@DrawableRes int imageRes) {
            optionBuilder.showImageOnLoading(imageRes);
            return this;
        }

        public Builder showImageOnFail(@DrawableRes int imageRes) {
            optionBuilder.showImageOnFail(imageRes);
            return this;
        }

        public Builder cacheInMemory(boolean cacheInMemory) {
            optionBuilder.cacheInMemory(cacheInMemory);
            return this;
        }

        public Builder cacheOnDisk(boolean cacheOnDisk) {
            optionBuilder.cacheOnDisk(cacheOnDisk);
            return this;
        }

        public Builder crossFade(int crossFade) {
            optionBuilder.crossFade(crossFade);
            return this;
        }
    }

}
