package com.service;

import com.data.User;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GithubService {
    @POST("SmartHome/sraum_getToken")
    Call<User> createUser(@Body Map<String, Object> map);
}
