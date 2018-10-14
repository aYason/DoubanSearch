package com.yason.doubanmovie.common.net;

import java.util.WeakHashMap;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * @author Yason
 * @since 2018/1/29
 */

public interface RestService {

  /** 1. Url + Get + Params */
  @GET
  Call<String> get(@Url String url, @QueryMap WeakHashMap<String, Object> params);

  /** 2. Url + Post + Params */
  @FormUrlEncoded
  @POST
  Call<String> post(@Url String url, @FieldMap WeakHashMap<String, Object> params);

  /** 3. Url + Post + JSON */
  @POST
  Call<String> postRaw(@Url String url, @Body RequestBody body);

  /** 4. Url + Post + Multipart */
  @Multipart
  @POST
  Call<String> upload(@Url String url, @Part MultipartBody.Part file);

  @Streaming
  @GET
  Call<ResponseBody> download(@Url String url, @QueryMap WeakHashMap<String, Object> params);

}
