package com.yason.doubanmovie.common.imageloader;

import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;

/**
 * Created by yasonxu on 2018/7/16.
 */
public final class LoaderOption {
    private final int imageResForEmptyUri;
    private final int imageResOnLoading;
    private final int imageResOnFail;
    private final boolean cacheInMemory;
    private final boolean cacheOnDisk;
    private final int crossFade;

    LoaderOption(Builder builder) {
        this.imageResForEmptyUri = builder.imageResForEmptyUri;
        this.imageResOnLoading = builder.imageResOnLoading;
        this.imageResOnFail = builder.imageResOnFail;
        this.cacheInMemory = builder.cacheInMemory;
        this.cacheOnDisk = builder.cacheOnDisk;
        this.crossFade = builder.crossFade;
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean shouldShowImageForEmptyUri() {
        return this.imageResForEmptyUri != 0;
    }

    public boolean shouldShowImageOnLoading() {
        return this.imageResOnLoading != 0;
    }

    public boolean shouldShowImageOnFail() {
        return this.imageResOnFail != 0;
    }

    public int getImageResForEmptyUri() {
        return this.imageResForEmptyUri;
    }

    public int getImageResOnLoading() {
        return this.imageResOnLoading;
    }

    public int getImageResOnFail() {
        return this.imageResOnFail;
    }

    public boolean isCacheInMemory() {
        return this.cacheInMemory;
    }

    public boolean isCacheOnDisk() {
        return this.cacheOnDisk;
    }

    public int getCrossFade() {
        return this.crossFade;
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    public static class Builder {
        private int imageResForEmptyUri;
        private int imageResOnLoading;
        private int imageResOnFail;
        private boolean cacheInMemory;
        private boolean cacheOnDisk;
        private int crossFade;

        Builder() {
            this.imageResForEmptyUri = 0;
            this.imageResOnLoading = 0;
            this.imageResOnFail = 0;
            this.cacheInMemory = true;
            this.cacheOnDisk = true;
            this.crossFade = 0;
        }

        Builder(LoaderOption loaderOption) {
            this.imageResForEmptyUri = loaderOption.getImageResForEmptyUri();
            this.imageResOnLoading = loaderOption.getImageResOnLoading();
            this.imageResOnFail = loaderOption.getImageResOnFail();
            this.cacheInMemory = loaderOption.isCacheInMemory();
            this.cacheOnDisk = loaderOption.isCacheOnDisk();
            this.crossFade = loaderOption.getCrossFade();
        }

        public Builder showImageForEmptyUri(@DrawableRes int imageRes) {
            this.imageResForEmptyUri = imageRes;
            return this;
        }

        public Builder showImageOnLoading(@DrawableRes int imageRes) {
            this.imageResOnLoading = imageRes;
            return this;
        }

        public Builder showImageOnFail(@DrawableRes int imageRes) {
            this.imageResOnFail = imageRes;
            return this;
        }

        public Builder cacheInMemory(boolean cacheInMemory) {
            this.cacheInMemory = cacheInMemory;
            return this;
        }

        public Builder cacheOnDisk(boolean cacheOnDisk) {
            this.cacheOnDisk = cacheOnDisk;
            return this;
        }

        public Builder crossFade(int crossFade) {
            this.crossFade = crossFade;
            return this;
        }

        public LoaderOption build() {
            return new LoaderOption(this);
        }

    }

}
