package com.chathurangashan.backgroundtasks

import android.app.Application
import android.content.Context
import com.chathurangashan.backgroundtasks.data.enums.BuildType
import com.chathurangashan.backgroundtasks.network.ApiService
import com.chathurangashan.backgroundtasks.network.NetworkService

class ThisApplication: Application()  {

    val sharedPreferences by lazy { getSharedPreferences(packageName, Context.MODE_PRIVATE) }
    val networkService by lazy { NetworkService.getInstance(this,sharedPreferences).getService(ApiService::class.java) }

    companion object {
        val buildType: BuildType = BuildType.RELEASE
    }

}