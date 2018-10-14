package com.yason.doubanmovie.common.loader;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import com.yason.doubanmovie.R;
import com.yason.doubanmovie.common.util.dimen.DimenUtil;
import java.util.ArrayList;

/**
 * @author Yason
 * @since 2018/1/30
 */

public final class Loader {

  private static final ArrayList<AppCompatDialog> LOADERS = new ArrayList<>();
  private static final LoaderStyle DEFAULT_LOADER_STYLE = LoaderStyle.BallPulseIndicator;

  private static final int LOADER_SIZE_SCALE = 8;
  private static final int LOADER_OFFSET_SCALE = 10;

  public static void showLoading(Context context) {
    showLoading(context, DEFAULT_LOADER_STYLE);
  }

  public static void showLoading(Context context, LoaderStyle style) {
    final AppCompatDialog dialog = new AppCompatDialog(context, R.style.dialog);
    dialog.setContentView(LoaderHolder.create(context, style));

    final Window dialogWindow = dialog.getWindow();
    if (dialogWindow != null) {
      final int deviceWidth = DimenUtil.getScreenWidth();
      final int deviceHeight = DimenUtil.getScreenHeight();

      final WindowManager.LayoutParams lp = dialogWindow.getAttributes();
      lp.width = deviceWidth / LOADER_SIZE_SCALE;
      lp.height = deviceHeight / LOADER_SIZE_SCALE;
      lp.height = lp.height + deviceHeight / LOADER_OFFSET_SCALE;
      lp.gravity = Gravity.CENTER;
    }
    LOADERS.add(dialog);
    dialog.show();

  }

  public static void stopLoading() {
    for (AppCompatDialog dialog : LOADERS) {
      if (dialog != null && dialog.isShowing()) {
        dialog.cancel();
      }
    }
  }

}
