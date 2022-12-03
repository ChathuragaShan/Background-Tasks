package com.chathurangashan.backgroundtasks.data.moshi

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class InboxResponse(
    @Json(name = "loyalty")
    val loyalty: List<MessageContent>,
    @Json(name = "offers")
    val offers: List<MessageContent>
)