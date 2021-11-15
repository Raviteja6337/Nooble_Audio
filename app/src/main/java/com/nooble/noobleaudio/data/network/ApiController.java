package com.nooble.noobleaudio.data.network;

import android.content.Context;


public class ApiController {

    private static ApiInterface aPIInterface;
    private final Context mContext;

    private static final String BASE_URL = "https://run.mocky.io/";

    public ApiController(Context context) {
        this.mContext = context;
    }

    public ApiInterface getApiInterface() {
        return (aPIInterface == null) ? getApiConnection() : aPIInterface;
    }

    private ApiInterface getApiConnection() {
        //todo:env basedUURL is pending

        aPIInterface = RestService.createRetrofitService(
                ApiInterface.class,
                BASE_URL
        );
        return aPIInterface;
    }


}
