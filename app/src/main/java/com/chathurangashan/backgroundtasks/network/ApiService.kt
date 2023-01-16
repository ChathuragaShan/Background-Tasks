package com.chathurangashan.backgroundtasks.network

import com.chathurangashan.backgroundtasks.data.moshi.DeliveryRecodeRequest
import com.chathurangashan.backgroundtasks.data.moshi.DeliveryRecodeResponse
import okhttp3.ResponseBody
import retrofit2.http.*

interface ApiService {

    @Streaming
    @GET("download/image/{image_name}")
    suspend fun downloadImage(@Path("image_name") imageName: String): ResponseBody

    @Streaming
    @GET("download/video/{video_name}")
    suspend fun downloadVideo(@Path("video_name") videoName: String): ResponseBody

    @POST("backup/work_history")
    suspend fun updateDeliveryRequestData(@Body deliveryRecodeRequest: List<DeliveryRecodeRequest>): DeliveryRecodeResponse

}