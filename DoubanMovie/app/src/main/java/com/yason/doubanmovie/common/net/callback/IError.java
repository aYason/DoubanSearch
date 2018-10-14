package com.yason.doubanmovie.common.net.callback;

/**
 * @author Yason
 * @since 2018/1/29
 */

public interface IError {

  /** 服务器错误 */
  void onError(int code, String msg);
}
