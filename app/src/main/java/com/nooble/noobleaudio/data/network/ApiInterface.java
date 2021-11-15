package com.nooble.noobleaudio.data.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * * @author Ankit Jangid
 * * updated on : Sept.29,2021
 *
 * <p>
 * Summary:  API end points and function calls declarations
 * </p>
 */

public interface ApiInterface {

    String GET_SHORT_DETAILS = "v3/b9f74279-038b-4590-9f96-7c720261294c/";


    @GET
    Call<ResponseBody> getShortDetails(@Url String url);

}
