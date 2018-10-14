package com.yason.doubanmovie.common.imageloader.callback;


/**
 * Created by yasonxu on 2018/7/19.
 */
public abstract class GlobalLoadingMonitorListener {
    public void onLoadingStarted(String uri){}
    public void onLoadingFailed(String uri, Exception e){}
    public void onLoadingSuccess(String uri) {}
    public void onLoadingCancelled(String uri) {}
}
