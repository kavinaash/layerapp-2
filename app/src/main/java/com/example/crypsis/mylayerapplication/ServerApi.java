package com.example.crypsis.mylayerapplication;

import com.layer.sdk.messaging.Conversation;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by crypsis on 26/9/16.
 */

public interface ServerApi {
    @Headers({ "Accept: application/vnd.layer+json; version=1.1",

    })
    @POST("/apps/layer:///apps/staging/170be0e8-6846-11e6-a7a9-d9a4c50e244c/conversations")
    Call<Conversation> createUser(@Body NewUserModel newUserModel);

}
