package com.nasa.demo.data.remote.api

import com.nasa.demo.BuildConfig
import com.nasa.demo.data.model.MarsPhotoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NasaApiService {
    @GET("rovers/curiosity/photos")
    suspend fun getMarsPhotos(
        @Query("sol") sol: Int,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): Response<MarsPhotoResponse>
}