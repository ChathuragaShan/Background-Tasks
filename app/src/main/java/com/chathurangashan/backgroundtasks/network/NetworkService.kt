package com.chathurangashan.backgroundtasks.network

import android.content.Context
import android.content.SharedPreferences
import com.chathurangashan.backgroundtasks.ThisApplication
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import com.chathurangashan.backgroundtasks.data.enums.BuildType
import retrofit2.converter.moshi.MoshiConverterFactory

private const val URL_DEV = ""
private const val URL_LIVE = "https://dummyapi.io/data/api/"

class NetworkService {

    private var baseURL: String
    private lateinit var context: Context
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        fun getInstance(context: Context,sharedPreferences: SharedPreferences): NetworkService {
            return NetworkService(context, sharedPreferences)
        }

        fun getTestInstance(testUrl: HttpUrl): NetworkService {
            return NetworkService(testUrl)
        }
    }

    constructor(){
        baseURL = when(ThisApplication.buildType){
            BuildType.RELEASE -> URL_LIVE
            BuildType.DEVELOPMENT -> URL_DEV
            BuildType.TESTING -> ""
        }
    }

    constructor(testUrl: HttpUrl) : this() {
        baseURL = testUrl.toString()
    }

    constructor(context: Context,sharedPreferences: SharedPreferences) : this() {
        this.context = context
        this.sharedPreferences = sharedPreferences
    }

    fun <S> getService(serviceClass: Class<S>): S {

        val httpBuilder = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)

        if (::context.isInitialized){
            httpBuilder.addInterceptor(ConnectivityInterceptor(context))
        }

        if(::sharedPreferences.isInitialized){
            httpBuilder.addInterceptor(MockInterceptor(context,sharedPreferences))
        }

        val builder = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl(baseURL)
            .client(httpBuilder.build())

        val retrofit = builder.build()

        return retrofit.create(serviceClass)
    }

}