package com.zenscale.zencrm_2.utils;

import com.squareup.okhttp.logging.HttpLoggingInterceptor;
import okhttp3.*;
import okio.Buffer;

import java.io.IOException;
import java.nio.charset.Charset;


public class CurlLoggerInterceptor implements Interceptor {

    private StringBuilder curlCommandBuilder;
    private final Charset UTF8 = Charset.forName("UTF-8");
    private String tag = null;
    private HttpLoggingInterceptor.Logger logger;




    public CurlLoggerInterceptor(HttpLoggingInterceptor.Logger httpLoggingInterceptor) {

        this.logger = httpLoggingInterceptor;
    }




    /**
     * Set logcat tag for curl lib to make it ease to filter curl logs only.
     *
     * @param tag
     */
    public CurlLoggerInterceptor(String tag) {

        this.tag = tag;
    }




    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();

        curlCommandBuilder = new StringBuilder("");
        // add cURL command
        curlCommandBuilder.append("curl ");
        curlCommandBuilder.append("-X ");
        // add method
        curlCommandBuilder.append(request.method().toUpperCase() + " ");
        // adding headers
        for (String headerName : request.headers().names()) {
            addHeader(headerName, request.headers().get(headerName));
        }

        // adding request body
        RequestBody requestBody = request.body();
        if (request.body() != null) {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            Charset charset = UTF8;
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                addHeader("Content-Type", request.body().contentType().toString());
                charset = contentType.charset(UTF8);
                curlCommandBuilder.append(" -d '" + buffer.readString(charset) + "'");
            }
        }

        // add request URL
        curlCommandBuilder.append(" \"" + request.url().toString() + "\"");
        curlCommandBuilder.append(" -L");

        logger.log(curlCommandBuilder.toString());
//        CurlPrinter.print(tag, request.url().toString(), curlCommandBuilder.toString());
        return chain.proceed(request);
    }




    private void addHeader(String headerName, String headerValue) {

        curlCommandBuilder.append("-H " + "\"" + headerName + ": " + headerValue + "\" ");
    }




}