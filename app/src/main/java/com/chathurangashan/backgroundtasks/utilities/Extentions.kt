package com.chathurangashan.backgroundtasks.utilities

import android.annotation.SuppressLint
import android.content.Context
import java.io.InputStream
import java.nio.charset.Charset

fun InputStream.readTextAndClose(charset: Charset = Charsets.UTF_8): String {
    return this.bufferedReader(charset).use { it.readText() }
}

@SuppressLint("DiscouragedApi")
fun Context.readJsonFileFromRawResource(fileName: String): String{
    val inputStream = resources.openRawResource(
        resources.getIdentifier(fileName, "raw", packageName)
    )

    return inputStream.readTextAndClose()
}