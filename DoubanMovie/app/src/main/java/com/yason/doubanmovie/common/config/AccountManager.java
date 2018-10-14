package com.yason.doubanmovie.common.config;


import com.yason.doubanmovie.common.util.storage.SharePreferenceUtil;

/**
 * @author Yason
 * @since 2018/3/5
 */

public class AccountManager {

  private enum SignTag {
    SIGN_TAG
  }

  public static void setSignState(boolean state) {
    SharePreferenceUtil.saveAppBoolean(SignTag.SIGN_TAG.name(), state);
  }

  public static boolean isSignIn() {
    return SharePreferenceUtil.getAppBoolean(SignTag.SIGN_TAG.name());
  }

  public static void checkAccount(IUserChecker checker) {
    if (isSignIn()) {
      checker.onSignIn();
    } else {
      checker.onNotSignIn();
    }
  }

}
