package com.yason.doubanmovie.common.net.callback;

/**
 * @author Yason
 * @since 2018/1/29
 */

public interface IFailure {

  /** 本地错误 */
  void onFailure(Throwable t);
}
