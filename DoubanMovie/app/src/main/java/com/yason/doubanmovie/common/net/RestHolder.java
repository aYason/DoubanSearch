package com.yason.doubanmovie.common.net;

import com.yason.doubanmovie.common.config.Config;
import com.yason.doubanmovie.common.config.ConfigKeys;
import com.yason.doubanmovie.common.net.interceptor.CookieIntercepter;
import java.util.ArrayList;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * @author Yason
 * @since 2018/1/29
 */

public final class RestHolder {

  public static RestService getService() {
    return ServiceHolder.REST_SERVICE;
  }

  /**
   * OkHttp
   */
  private static final class OkHttpHolder {

    private static final ArrayList<Interceptor> INTERCEPTORS = Config
        .getConfiguration(ConfigKeys.INTERCEPTORS);
    private static final OkHttpClient.Builder BUILDER = new OkHttpClient.Builder();
    private static final OkHttpClient OK_HTTP_CLIENT = addInterceptors().build();

    private static OkHttpClient.Builder addInterceptors() {
      BUILDER.addInterceptor(new CookieIntercepter());
      if (INTERCEPTORS != null && !INTERCEPTORS.isEmpty()) {
        for (Interceptor interceptor : INTERCEPTORS) {
          BUILDER.addInterceptor(interceptor);
        }
      }
      return BUILDER;
    }
  }

  /**
   * Retrofit
   */
  private static final class RetrofitHolder {

    private static final String BASE_URL = Config.getConfiguration(ConfigKeys.API_HOST);
    private static final Retrofit RETROFIT = new Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(OkHttpHolder.OK_HTTP_CLIENT)
        .addConverterFactory(ScalarsConverterFactory.create())//使用纯文本转换器
        .build();
  }

  /**
   * Service
   */
  private static final class ServiceHolder {

    private static final RestService REST_SERVICE =
        RetrofitHolder.RETROFIT.create(RestService.class);
  }

}
