package com.cts.teamplayer.network

import android.content.Context
import com.cts.teamplayer.util.MyConstants.BASE_URL
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import java.util.concurrent.TimeUnit


class ApiClient {


    companion object aa{
        private var retrofit: Retrofit? = null

        fun getClient(context: Context): Retrofit? {

            if (retrofit == null) {
                val httpClient = OkHttpClient.Builder()
                httpClient.addInterceptor { chain ->
                    val originalRequest = chain.request()
                    val request = originalRequest.newBuilder()
                        //.addHeader("XAPIKEY", MyConstants.X_API_KEY)
                        .method(originalRequest.method(), originalRequest.body())
                        .build()

                    chain.proceed(request)
                }
                httpClient.connectTimeout(50, TimeUnit.SECONDS)
                    .readTimeout(50, TimeUnit.SECONDS)
                    .writeTimeout(50, TimeUnit.SECONDS)
                    .build()
                val client = httpClient.build()
                val gson = GsonBuilder()
                    .setLenient()
                    .create()
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build()
            }
            return retrofit
        }

        fun getConnection(context: Context): ApiInterface? {
            return getClient(context)?.create(ApiInterface::class.java)
        }
    }



}
