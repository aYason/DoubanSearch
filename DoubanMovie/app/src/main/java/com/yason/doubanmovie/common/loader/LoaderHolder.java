package com.yason.doubanmovie.common.loader;

import android.content.Context;
import com.wang.avi.AVLoadingIndicatorView;
import com.wang.avi.Indicator;
import java.util.WeakHashMap;

/**
 * @author Yason
 * @since 2018/1/31
 */

final class LoaderHolder {

  private static final WeakHashMap<String, Indicator> INDICATORS = new WeakHashMap<>();

  static AVLoadingIndicatorView create(Context context, LoaderStyle style) {

    final AVLoadingIndicatorView avLoadingIndicatorView = new AVLoadingIndicatorView(context);

    Indicator indicator = INDICATORS.get(style.name());

    if (indicator == null) {
      indicator = getIndicator(style);
      INDICATORS.put(style.name(), indicator);
    }

    avLoadingIndicatorView.setIndicator(indicator);

    return avLoadingIndicatorView;
  }


  @SuppressWarnings("unchecked")
  private static Indicator getIndicator(LoaderStyle style) {
    if (style == null) {
      return null;
    }

    final StringBuilder indicatorClassName = new StringBuilder();
    final String packageName = AVLoadingIndicatorView.class.getPackage().getName();

    indicatorClassName
        .append(packageName)
        .append(".indicators")
        .append(".")
        .append(style.name());

    try {
      final Class<Indicator> clazz =
          (Class<Indicator>) Class.forName(indicatorClassName.toString());
      return clazz.newInstance();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;

  }
}
