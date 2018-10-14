package com.yason.doubanmovie;

import android.app.Application;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.yason.doubanmovie.common.config.Config;
import com.yason.doubanmovie.common.imageloader.ImageLoader;
import com.yason.doubanmovie.common.net.interceptor.MockInterceptor;

/**
 * @author Yason
 * @since 2018/10/8
 */

public class App extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    Config.init(this)
        .withApiHost("http://192.168.43.53:8000")
        .withIcon(new FontAwesomeModule())
        .configure();
    ImageLoader.getInstance().init(null);
  }
}
