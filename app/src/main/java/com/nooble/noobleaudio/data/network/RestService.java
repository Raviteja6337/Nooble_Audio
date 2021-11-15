
package com.nooble.noobleaudio.data.network;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RestService {

    private static OkHttpClient getRequestHeader() {
        return new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    public static <T> T createRetrofitService(final Class<T> clazz, final String endPoint) {

        /*Retrofit*/
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = (new Retrofit.Builder())
                .baseUrl(endPoint)
                .client(getRequestHeader())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        T service = retrofit.create(clazz);
        Log.d("RestService", "response service:- " + service);
        return service;
    }

    /*
     *  Sending parameter in raw in JSON String
     * */

    public static RequestBody requestBodyJsonObject(String jsonParams) {
        Log.d("RestService", "" + jsonParams);
        return RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonParams);
    }

}
