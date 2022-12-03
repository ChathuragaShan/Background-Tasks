package com.chathurangashan.backgroundtasks.data.moshi

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class MessageContent(
    @Json(name = "id")
    val id: Int,
    @Json(name = "description")
    val description: String,
    @Json(name = "image_link")
    val imageLink: String,
    @Json(name = "title")
    val title: String
) : Parcelable