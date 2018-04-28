package com.pwds.ultimate.securityassistant.Api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

import com.pwds.ultimate.securityassistant.Model.CommunityPostDto;
import com.pwds.ultimate.securityassistant.Model.LoginDto;
import com.pwds.ultimate.securityassistant.Model.RegisterDto;

import java.util.ArrayList;

public interface Api {

    @POST("users/login")
    Call<RegisterDto> sendUsernameAndPassword(@Body LoginDto loginDto);

    @POST("users/register")
    Call<RegisterDto> userRegister(@Body RegisterDto registerDto);

    @GET("community")
    Call<ArrayList<CommunityPostDto>> getCommunityPost();

    @POST("community")
    Call<String> postCommunityMessage(@Header("userId") Long userId , @Body CommunityPostDto communityPostDto);

    @GET("visibility/{lat}/{lng}")
    Call<ArrayList<RegisterDto>> getNearBY(@Path("lat") Double lat, @Path("lng") Double lng);

}