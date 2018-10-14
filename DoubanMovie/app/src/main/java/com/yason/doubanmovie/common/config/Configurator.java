package com.yason.doubanmovie.common.config;

import android.app.Activity;
import android.content.Context;
import com.joanzapata.iconify.IconFontDescriptor;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.Iconify.IconifyInitializer;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import okhttp3.Interceptor;

/**
 * @author Yason
 * @since 2018/1/27
 */

public final class Configurator {

  private final HashMap<ConfigKeys, Object> GLOBAL_CONFIG = new HashMap<>();
  private final ArrayList<IconFontDescriptor> ICONS = new ArrayList<>();
  private final ArrayList<Interceptor> INTERCEPTORS = new ArrayList<>();

  private Configurator() {
    GLOBAL_CONFIG.put(ConfigKeys.CONFIG_READY, false);
    GLOBAL_CONFIG.put(ConfigKeys.INTERCEPTORS, INTERCEPTORS);
  }

  Configurator withApplicationContext(Context context) {
    GLOBAL_CONFIG.put(ConfigKeys.APPLICATION_CONTEXT, context);
    return this;
  }

  public Configurator withApiHost(String host) {
    GLOBAL_CONFIG.put(ConfigKeys.API_HOST, host);
    return this;
  }

  public Configurator withActivity(Activity activity) {
    GLOBAL_CONFIG.put(ConfigKeys.ACTIVITY, activity);
    return this;
  }

  public Configurator withIcon(IconFontDescriptor descriptor) {
    ICONS.add(descriptor);
    return this;
  }

  public Configurator withInterceptor(Interceptor interceptor) {
    INTERCEPTORS.add(interceptor);
    return this;
  }

  public void configure() {
    initIcons();
    Logger.addLogAdapter(new AndroidLogAdapter());
    GLOBAL_CONFIG.put(ConfigKeys.CONFIG_READY, true);
  }

  private void initIcons() {
    final int size = ICONS.size();
    if (size > 0) {
      IconifyInitializer initializer = Iconify.with(ICONS.get(0));
      for (int index = 1; index < size; index++) {
        initializer.with(ICONS.get(index));
      }
    }
  }

  <T> T getConfiguration(ConfigKeys key) {
    checkConfiguration();
    Object value = GLOBAL_CONFIG.get(key);
    if (value == null) {
      throw new NullPointerException(key.name() + "IS NULL!");
    }
    return (T) value;
  }

  private void checkConfiguration() {
    final boolean isReady = (boolean) GLOBAL_CONFIG.get(ConfigKeys.CONFIG_READY);
    if (!isReady) {
      throw new IllegalStateException("Configuration is not ready,call configure!");
    }
  }

//  HashMap<ConfigKeys, Object> getConfigs() {
//    return GLOBAL_CONFIG;
//  }

  static Configurator getInstance() {
    return Holder.INSTANCE;
  }

  private static final class Holder{
    private static final Configurator INSTANCE = new Configurator();
  }

}
