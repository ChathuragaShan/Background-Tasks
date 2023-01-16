package com.chathurangashan.backgroundtasks.network

import android.content.Context
import android.content.SharedPreferences
import com.chathurangashan.backgroundtasks.R
import com.chathurangashan.backgroundtasks.data.moshi.DeliveryRecodeRequest
import com.chathurangashan.backgroundtasks.data.moshi.DeliveryRecodeResponse
import com.squareup.moshi.Moshi
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import java.io.InputStream

/**
 * Network interceptor class that act as a remote server which supply the response accordingly
 */
class MockInterceptor (val context: Context,val sharedPreferences: SharedPreferences) : Interceptor {

    private var attempts = 0
    private fun wantRandomError() = attempts++ % 5 == 0

    override fun intercept(chain: Interceptor.Chain): Response {

        Thread.sleep(1000) // fake delay

        val request = chain.request()
        val url = request.url.toString()

        if (url.contains("download/image/")) {
            val image = request.url.pathSegments[4]
            return processDownloadImageResponse(request,image)
        }else if(url.endsWith("backup/work_history")){
            val loginRequestBody = getRequestBody<List<DeliveryRecodeRequest>>(request)
            return processDeliveryRecodeResponse(request,loginRequestBody)
        }else if(url.contains("download/video/")){
            val image = request.url.pathSegments[4]
            return processDownloadVideoResponse(request,image)
        }

        return chain.proceed(request)
    }

    private fun processDownloadImageResponse(request: Request, image: String): Response{

        if(image == "wallpaper"){

            val imageStream: InputStream = context.resources.openRawResource(R.raw.wallpaper)

            return Response.Builder()
                .code(200)
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .message("Success")
                .body(imageStream.readBytes().toResponseBody("image/jpg".toMediaType()))
                .build()

        }else{

            return Response.Builder()
                .code(200)
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .message("Image not found")
                .body(null)
                .build()
        }
    }

    private fun processDownloadVideoResponse(request: Request, video: String): Response{

        if(video == "short_film"){

            val imageStream: InputStream = context.resources.openRawResource(R.raw.wallpaper)

            return Response.Builder()
                .code(200)
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .message("Success")
                .body(imageStream.readBytes().toResponseBody("image/jpg".toMediaType()))
                .build()

        }else{

            return Response.Builder()
                .code(200)
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .message("Image not found")
                .body(null)
                .build()
        }
    }

    private fun processDeliveryRecodeResponse(request: Request, deliveryRecodeRequest: List<DeliveryRecodeRequest>): Response {

        val moshi = Moshi.Builder().build()
        val jsonAdapter = moshi.adapter(DeliveryRecodeResponse::class.java)

        if(deliveryRecodeRequest.isNotEmpty()){

            val deliveryRecodeResponse = DeliveryRecodeResponse(true, "data sync is successful")

            return Response.Builder()
                .code(200)
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .message("Success")
                .body(jsonAdapter.toJson(deliveryRecodeResponse)
                    .toResponseBody("application/json".toMediaType()))
                .build()

        }else{

            val deliveryRecodeResponse = DeliveryRecodeResponse(false, "Invalid data")

            return Response.Builder()
                .code(200)
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .message("Input Field Error")
                .body(jsonAdapter.toJson(deliveryRecodeResponse)
                    .toResponseBody("application/json".toMediaType()))
                .build()
        }
    }

    /**
     * responsible for getting the request body as a moshi object
     *
     * @param request: Retrofit request
     */
    private inline fun <reified T> getRequestBody(request: Request): T {

        val copy: Request = request.newBuilder().build()
        val buffer = Buffer()
        copy.body!!.writeTo(buffer)
        val requestBodyString = buffer.readUtf8()

        val moshi = Moshi.Builder().build()
        val jsonAdapter = moshi.adapter(T::class.java)
        return jsonAdapter.fromJson(requestBodyString) as T
    }

    private fun randomServerError(request: Request): Response {

        return Response.Builder()
                .code(500)
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .message("Internal server error")
                .body("{}".toResponseBody("application/json".toMediaType()))
                .build()
    }

}