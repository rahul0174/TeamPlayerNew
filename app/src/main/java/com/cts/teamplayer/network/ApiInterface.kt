package com.cts.teamplayer.network

import com.cts.teamplayer.models.*
import com.google.gson.JsonArray
import com.google.gson.JsonObject

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*


interface ApiInterface {

    @GET("countries")
    fun getCountry(): Call<CountryListResponse>
    @GET("sector")
    fun getSector(): Call<SectorListResponse>
    @GET("occupation")
    fun getOccupation(): Call<OccupationResponse>
    @GET("states")
    fun getState(@Query("country_id") city_id: String,): Call<StateListResponse>
    @GET("cities")
    fun getCity(@Query("state_id") state_id: String,): Call<CityListResponse>

    @Multipart
    @POST("user/upload")
    fun uploadSingleFile(@Part list: List<MultipartBody.Part>): Call<JsonObject>

    @POST("user/register")
    fun userSignup(@Body data: JsonObject): Call<JsonObject>

    @POST("user/login")
    fun login(@Body data: JsonObject): Call<JsonObject>

    @POST("user/forgot_password")
    fun forgotpass(@Body data: JsonObject): Call<JsonObject>


}