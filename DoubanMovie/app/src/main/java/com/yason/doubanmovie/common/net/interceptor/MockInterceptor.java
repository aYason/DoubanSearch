package com.yason.doubanmovie.common.net.interceptor;

import android.support.annotation.RawRes;
import com.yason.doubanmovie.common.util.file.FileUtil;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author Yason
 * @since 2018/1/31
 */

public class MockInterceptor implements Interceptor {

  private final String MOCK_URL;
  private final int MOCK_RAW_ID;

  public MockInterceptor(String mockUrl, @RawRes int rawId) {
    MOCK_URL = mockUrl;
    MOCK_RAW_ID = rawId;
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    final Request request = chain.request();
    final String url = request.url().toString();
    if (url.contains(MOCK_URL)) {
      return getMockResponse(chain, MOCK_RAW_ID);
    } else {
      return chain.proceed(request);
    }
  }

  private Response getMockResponse(Chain chain, @RawRes int rawId) {
    final String json = FileUtil.getRawFile(rawId);
    return new Response.Builder()
        .request(chain.request())
        .code(200)
        .message("OK")
        .protocol(Protocol.HTTP_1_1)
        .header("Content-Type", "application/json")
        .body(ResponseBody.create(MediaType.parse("application/json"), json))
        .build();
  }

}
