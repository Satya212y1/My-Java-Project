package com.zenscale.zencrm_2.utils;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

public interface ApiCalls {

    @Multipart
    @POST
    Call<ResponseBody> postFormData(@Url String url, @PartMap Map<String, RequestBody> partmap, @Header("Authorization") String authToken);

    @POST
    Call<ResponseBody> postBodyData(@Url String url, @Body Object body, @Header("Authorization") String authToken);

    @GET
    Call<ResponseBody> getQueryData(@Url String url, @QueryMap Map<String, Object> map, @Header("Authorization") String authToken);

    @DELETE
    Call<ResponseBody> delQueryData(@Url String url, @QueryMap Map<String, Object> map, @Header("Authorization") String authToken);




}
