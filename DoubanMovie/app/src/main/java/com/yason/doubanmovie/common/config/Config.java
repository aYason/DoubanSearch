package com.yason.doubanmovie.common.config;

import android.content.Context;

/**
 * @author Yason
 * @since 2018/1/27
 */

public final class Config {

  private Config() {}

  public static Configurator init(Context context) {
    Configurator.getInstance()
        .withApplicationContext(context);
    return Configurator.getInstance();
  }

  public static Context getApplicationContext() {
    return getConfiguration(ConfigKeys.APPLICATION_CONTEXT);
  }

  public static Configurator getConfigurator() {
    return Configurator.getInstance();
  }

  public static <T> T getConfiguration(ConfigKeys key) {
    return Configurator.getInstance().getConfiguration(key);
  }

}
