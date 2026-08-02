package com.example.hockeygame.game.network;

import com.example.hockeygame.game.network.model.LoginRequest;
import com.example.hockeygame.game.network.model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {

    @POST("api/auth/login")
    Call<LoginResponse> login(
            @Body LoginRequest request
    );
}