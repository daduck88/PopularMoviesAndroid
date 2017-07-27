package com.android.popularmoviesapp.rest;

import com.android.popularmoviesapp.BuildConfig;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Daduck on 7/26/17.
 */

public class RestClient {

    private static String BASE_URL;
    private ApiService apiService;
    private PublicService publicService;
    private String key;

    public RestClient(String baseUrl) {
        key = BuildConfig.API_KEY;
        BASE_URL = baseUrl;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getClient())
                .build();


        this.apiService = retrofit.create(ApiService.class);
        this.publicService = new PublicService(getApiService());
    }

    private ApiService getApiService() {
        return this.apiService;
    }

    public PublicService getPublicService() {
        return this.publicService;
    }

    private OkHttpClient getClient() {
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                HttpUrl url = request.url().newBuilder().addQueryParameter("api_key", key).build();
                request = request.newBuilder().url(url).build();
                return chain.proceed(request);
            }
        }).build();
        return client;
    }
}
