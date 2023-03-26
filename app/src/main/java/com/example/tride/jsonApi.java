package com.example.tride;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface jsonApi {
  @FormUrlEncoded
  @POST("travel/post")
  Call<ResponseBody>createPost(@Field("fromTime")Number fromTime);
}
