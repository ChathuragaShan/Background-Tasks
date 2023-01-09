package com.chathurangashan.backgroundtasks.utilities

import android.annotation.SuppressLint
import android.content.Context
import com.chathurangashan.backgroundtasks.data.general.DownloadState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.ResponseBody
import java.io.File
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

fun ResponseBody.saveFile(filePath: String): Flow<DownloadState> {
    return flow{
        emit(DownloadState.Downloading(0))
        val destinationFile = File(filePath)

        try {
            byteStream().use { inputStream->
                destinationFile.outputStream().use { outputStream->
                    val totalBytes = contentLength()
                    val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                    var progressBytes = 0L
                    var bytes = inputStream.read(buffer)
                    while (bytes >= 0) {
                        outputStream.write(buffer, 0, bytes)
                        progressBytes += bytes
                        bytes = inputStream.read(buffer)
                        emit(DownloadState.Downloading(((progressBytes * 100) / totalBytes).toInt()))
                    }
                }
            }
            emit(DownloadState.Finished)
        } catch (e: Exception) {
            emit(DownloadState.Failed(e))
        }
    }.flowOn(Dispatchers.IO).distinctUntilChanged()
}