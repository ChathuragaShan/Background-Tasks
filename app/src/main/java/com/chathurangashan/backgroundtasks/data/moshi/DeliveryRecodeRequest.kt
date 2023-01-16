package com.chathurangashan.backgroundtasks.data.moshi

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DeliveryRecodeRequest(
    @Json(name = "id")
    val id: Long,
    @Json(name = "address")
    val address: String,
    @Json(name = "nic")
    val nic: String,
    @Json(name = "delivery_type")
    val deliveryType: String
)
