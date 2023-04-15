package com.zenscale.zencrm_2.utils;

import com.google.gson.Gson;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@Service
public class RetrofitUtil {

    @Value("${spring.profiles.active}")
    private String profile;

    CommonFunctions cf = new CommonFunctions();




    private Retrofit getRetrofitInstance( String baseUrl) {

        String API_BASE_URL = baseUrl;

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = null;
        retrofit = builder.client(getUnsafeOkHttpClient()).build();

        return retrofit;
    }




    private OkHttpClient getUnsafeOkHttpClient() {

        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {

                        }




                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {

                        }




                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {

                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(1, TimeUnit.MINUTES);
            builder.readTimeout(1, TimeUnit.MINUTES);
            builder.writeTimeout(1, TimeUnit.MINUTES);
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier((String hostname, SSLSession session) -> true);
            builder.addInterceptor(new CurlLoggerInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {

                    System.out.println("message = " + message);
                }

            }));

            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }




    public JSONObject postFormData(String subUrl, Map<String, RequestBody> partmap, Object responseClass, String authToken , String baseUrl) {

        final BlockingQueue<JSONObject> blockingQueue = new ArrayBlockingQueue<>(1);
        JSONObject jsonResponse = new JSONObject();

        getRetrofitInstance(baseUrl)
                .create(ApiCalls.class)
                .postFormData(subUrl, partmap, authToken)
                .enqueue(new Callback<ResponseBody>() {

                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                String res = cf.convertStreamToString(response.body().byteStream());
                                jsonResponse.put("response", new Gson().fromJson(res, responseClass.getClass()));
                                jsonResponse.put("error", "");
                            } else {
                                jsonResponse.put("response", "");
                                jsonResponse.put("error", "Response is null");
                            }
                        } else {
                            jsonResponse.put("response", "");
                            jsonResponse.put("error", cf.convertStreamToString(response.errorBody().byteStream()));
                        }

                        blockingQueue.add(jsonResponse);
                    }




                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable throwable) {

                        jsonResponse.put("response", "");
                        jsonResponse.put("error", throwable.getMessage());
                        blockingQueue.add(jsonResponse);
                    }
                });
        try {
            return blockingQueue.take();
        } catch (InterruptedException e) {
            return null;
        }
    }




    public JSONObject postBodyData(String subUrl, Object body, Object responseClass, String authToken,String baseUrl) {

        //System.out.println("body = " + body);
        final BlockingQueue<JSONObject> blockingQueue = new ArrayBlockingQueue<>(1);
        JSONObject jsonResponse = new JSONObject();

        getRetrofitInstance(baseUrl)
                .create(ApiCalls.class)
                .postBodyData(subUrl, body, authToken)
                .enqueue(new Callback<ResponseBody>() {

                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                String res = cf.convertStreamToString(response.body().byteStream());
//                                System.out.println("new Gson().fromJson(res, responseClass.getClass()) = " + new Gson().fromJson(res, responseClass.getClass()));
                                jsonResponse.put("response", new Gson().fromJson(res, responseClass.getClass()));
                                jsonResponse.put("error", "");
                            } else {
                                jsonResponse.put("response", "");
                                jsonResponse.put("error", "Response is null");
                            }
                        } else {
                            jsonResponse.put("response", "");
                            jsonResponse.put("error", cf.convertStreamToString(response.errorBody().byteStream()));
                        }

                        blockingQueue.add(jsonResponse);
                    }




                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable throwable) {

                        jsonResponse.put("response", "");
                        jsonResponse.put("error", throwable.getMessage());
                        blockingQueue.add(jsonResponse);
                    }
                });

        try {
            return blockingQueue.take();
        } catch (InterruptedException e) {
            return null;
        }
    }




    public JSONObject delQueryData(String subUrl, Map<String, Object> partmap, Object responseClass, String authToken, String baseUrl) {


        final BlockingQueue<JSONObject> blockingQueue = new ArrayBlockingQueue<>(1);
        JSONObject jsonResponse = new JSONObject();

        getRetrofitInstance(baseUrl)
                .create(ApiCalls.class)
                .delQueryData(subUrl, partmap, authToken)
                .enqueue(new Callback<ResponseBody>() {

                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                String res = cf.convertStreamToString(response.body().byteStream());
//                                System.out.println("new Gson().fromJson(res, responseClass.getClass()) = " + new Gson().fromJson(res, responseClass.getClass()));
                                jsonResponse.put("response", new Gson().fromJson(res, responseClass.getClass()));
                                jsonResponse.put("error", "");
                            } else {
                                jsonResponse.put("response", "");
                                jsonResponse.put("error", "Response is null");
                            }
                        } else {
                            jsonResponse.put("response", "");
                            jsonResponse.put("error", cf.convertStreamToString(response.errorBody().byteStream()));
                        }

                        blockingQueue.add(jsonResponse);
                    }




                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable throwable) {

                        jsonResponse.put("response", "");
                        jsonResponse.put("error", throwable.getMessage());
                        blockingQueue.add(jsonResponse);
                    }
                });

        try {
            return blockingQueue.take();
        } catch (InterruptedException e) {
            return null;
        }
    }




    public JSONObject getQueryData(String subUrl, Map<String, Object> map, Object responseClass, String authToken , String baseUrl) {

        final BlockingQueue<JSONObject> blockingQueue = new ArrayBlockingQueue<>(1);
        JSONObject jsonResponse = new JSONObject();

        getRetrofitInstance(baseUrl)
                .create(ApiCalls.class)
                .getQueryData(subUrl, map, authToken)
                .enqueue(new Callback<ResponseBody>() {

                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                String res = cf.convertStreamToString(response.body().byteStream());
                                jsonResponse.put("response", new Gson().fromJson(res, responseClass.getClass()));
                                jsonResponse.put("error", "");
                            } else {
                                jsonResponse.put("response", "");
                                jsonResponse.put("error", "Response is null");
                            }
                        } else {
                            jsonResponse.put("response", "");
                            jsonResponse.put("error", cf.convertStreamToString(response.errorBody().byteStream()));
                        }

                        blockingQueue.add(jsonResponse);
                    }




                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable throwable) {

                        jsonResponse.put("response", "");
                        jsonResponse.put("error", throwable.getMessage());
                        blockingQueue.add(jsonResponse);
                    }
                });
        try {
            return blockingQueue.take();
        } catch (InterruptedException e) {
            jsonResponse.put("response", "");
            jsonResponse.put("error", "error");
            return jsonResponse;
        }
    }




}
