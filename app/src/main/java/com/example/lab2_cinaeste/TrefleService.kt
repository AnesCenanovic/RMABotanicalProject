package com.example.lab2_cinaeste

import android.util.Log
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

public interface TrefleService {

    @GET("plants/search")
    suspend fun searchPlants(
        @Query("q") latinName: String,
        @Query("token") apiKey: String
    ): PlantResponse

    @GET("species/{id}")
    suspend fun getPlantDetails(
        @Path("id") id: Int?,
        @Query("token") apiKey: String
    ): PlantDetailsResponse

    @GET("plants")
    suspend fun getPlantsWithFlowerColor(
        @Query("filter[flower_color]") flowerColour: String,
        @Query("token") apiKey: String,
        @Query("page") page : Int
    ): PlantResponse

    object RetrofitClient {

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request()
                val url = request.url()
                //Log.d("TrefleDAO", "opened URL: $url")
                chain.proceed(request)
            }
            .build()

        val retrofit : Retrofit = Retrofit.Builder()
            .baseUrl("https://trefle.io/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        val trefleService: TrefleService by lazy {
            retrofit.create(TrefleService::class.java)
        }
    }
}