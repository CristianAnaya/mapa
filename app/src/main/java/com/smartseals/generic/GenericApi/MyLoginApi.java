package com.smartseals.generic.GenericApi;

import com.smartseals.generic.Modelo.UserModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface MyLoginApi {
    @FormUrlEncoded
    @POST("auth/login")
    Call<UserModel> PostUser(@Field("username") String userName,
                                   @Field("password") String password);
}
