package com.yason.doubanmovie.common.util.dimen;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import com.yason.doubanmovie.common.config.Config;

/**
 * @author Yason
 * @since 2018/1/29
 */

public final class DimenUtil {

  private DimenUtil() {}

  public static int getScreenWidth() {
    final Resources resources = Config.getApplicationContext().getResources();
    final DisplayMetrics dm = resources.getDisplayMetrics();
    return dm.widthPixels;
  }

  public static int getScreenHeight() {
    final Resources resources = Config.getApplicationContext().getResources();
    final DisplayMetrics dm = resources.getDisplayMetrics();
    return dm.heightPixels;
  }

  public static int px2dip(float pxValue) {
    final Resources resources = Config.getApplicationContext().getResources();
    final float scale = resources.getDisplayMetrics().density;
    return (int) (pxValue / scale + 0.5f);
  }

  public static int dip2px(float dipValue) {
    final Resources resources = Config.getApplicationContext().getResources();
    final float scale = resources.getDisplayMetrics().density;
    return (int) (dipValue * scale + 0.5f);
  }

}
