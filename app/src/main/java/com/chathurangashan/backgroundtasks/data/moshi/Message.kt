package com.chathurangashan.backgroundtasks.data.moshi

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Message(
    @Json(name = "id")
    val id: Int,
    @Json(name = "description")
    val description: String,
    @Json(name = "image_link")
    val imageLink: String,
    @Json(name = "title")
    val title: String
)