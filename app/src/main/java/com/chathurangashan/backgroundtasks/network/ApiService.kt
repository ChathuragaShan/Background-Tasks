package com.chathurangashan.backgroundtasks.network

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Streaming

interface ApiService {

    @Streaming
    @GET("images/download/{image}")
    suspend fun downloadImage(@Path("image") imageFileName: String): ResponseBody

}