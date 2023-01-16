package com.chathurangashan.backgroundtasks.data.moshi

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DeliveryRecodeResponse(
    @Json(name = "status")
    val status: Boolean,
    @Json(name = "message")
    val message: String
)
