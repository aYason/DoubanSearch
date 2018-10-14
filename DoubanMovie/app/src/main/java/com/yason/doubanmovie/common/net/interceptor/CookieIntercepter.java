package com.yason.doubanmovie.common.net.interceptor;

import com.yason.doubanmovie.common.util.storage.SharePreferenceUtil;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;

/**
 * @author Yason
 * @since 2018/3/6
 */

public class CookieIntercepter implements Interceptor {

  private enum CookieTag {
    COOKIE_TAG
  }

  @Override
  public Response intercept(Chain chain) throws IOException {

    final Request.Builder builder = chain.request().newBuilder();
    addCookie(builder);

    final Response response = chain.proceed(builder.build());
    saveCookie(response);

    return response;
  }

  private void addCookie(Builder builder) {
    final Set<String> cookies = SharePreferenceUtil.getAppStringSet(CookieTag.COOKIE_TAG.name());
    if (cookies != null && cookies.size() > 0) {
      for (String cookie : cookies) {
        builder.addHeader("Cookie", cookie);
      }
    }
  }

  private void saveCookie(Response response) {
    List<String> responseCookies = response.headers("Set-Cookie");
    if (responseCookies != null && !responseCookies.isEmpty()) {
      HashSet<String> cookies = new HashSet<>();
      for (String responseCookie : responseCookies) {
        cookies.add(responseCookie);
      }
      SharePreferenceUtil.saveAppStringSet(CookieTag.COOKIE_TAG.name(), cookies);
    }
  }

}
