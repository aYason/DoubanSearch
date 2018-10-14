package com.yason.doubanmovie.common.util.toast;


import android.widget.Toast;
import com.yason.doubanmovie.common.config.Config;

/**
 * @author Yason
 * @since 2018/1/27
 */

public final class ToastUtil {

  private static Toast mToast;

  private ToastUtil() {}

  public static void showLong(String text) {
    show(text, Toast.LENGTH_LONG);
  }

  public static void showShort(String text) {
    show(text, Toast.LENGTH_SHORT);
  }

  private static void show(String text, int length) {
    if (mToast == null) {
      mToast = Toast.makeText(Config.getApplicationContext(), text, length);
    } else {
      mToast.setText(text);
    }

    mToast.show();
  }

}
