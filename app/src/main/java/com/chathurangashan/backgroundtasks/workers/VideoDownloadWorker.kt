package com.chathurangashan.backgroundtasks.workers

import android.content.Context
import android.os.Environment
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.chathurangashan.backgroundtasks.ThisApplication
import com.chathurangashan.backgroundtasks.data.general.DownloadState
import com.chathurangashan.backgroundtasks.network.ApiService
import com.chathurangashan.backgroundtasks.network.ConnectivityInterceptor
import com.chathurangashan.backgroundtasks.utilities.saveFile
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.File
import java.io.IOException
import java.net.SocketTimeoutException

class VideoDownloadWorker (appContext: Context, workerParams: WorkerParameters): CoroutineWorker(appContext, workerParams)  {

    private val thisApplication = appContext as ThisApplication
    private lateinit var fileDownloadState: Result

    override suspend fun doWork(): Result = coroutineScope {

        val networkService by lazy { thisApplication.networkService }
        val imageURl =
            inputData.getString(VIDEO_NAME) ?: return@coroutineScope Result.failure()
        downloadVideo(networkService,imageURl)

        return@coroutineScope fileDownloadState

    }

    private suspend fun downloadVideo (networkService: ApiService, videoFileName: String) {

        try {

            val filePath = File(
                thisApplication.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
                File.separator.toString() + "$videoFileName.mp4"
            ).toString()

            networkService.downloadVideo(videoFileName).saveFile(filePath)
                .collect{

                    fileDownloadState = when(it){
                        is DownloadState.Downloading -> {
                            delay(250L)
                            setProgress(workDataOf(
                                DOWNLOAD_PROGRESS to it.progress)
                            )
                            Result.success()
                        }
                        DownloadState.Failed() -> {
                            Result.failure(
                                workDataOf(
                                    ERROR_MESSAGE to "File Download Error"
                                )
                            )
                        }
                        DownloadState.Finished -> {
                            Result.success(
                                workDataOf(
                                    DOWNLOADED_FILE_NAME to filePath
                                )
                            )
                        }
                        else -> {
                            Result.failure(
                                workDataOf(
                                ERROR_MESSAGE to "UnKnown Error"
                                )
                            )
                        }
                    }
                }

        } catch (exception: Exception) {

            fileDownloadState = when (exception) {
                is HttpException ->
                    Result.failure(workDataOf(ERROR_MESSAGE to "Network Error"))
                is ConnectivityInterceptor.NoConnectivityException ->
                    Result.failure(workDataOf(ERROR_MESSAGE to "No Connection Error"))
                is SocketTimeoutException ->
                    Result.retry()
                is IOException ->
                    Result.failure(workDataOf(ERROR_MESSAGE to "File Write Exception"))
                else ->
                    Result.failure(workDataOf(ERROR_MESSAGE to "UnKnown Error"))
            }
        }
    }

    companion object {
        const val DOWNLOAD_PROGRESS = "DOWNLOAD_PROGRESS"
        const val DOWNLOADED_FILE_NAME = "DOWNLOADED_FILE_NAME"
        const val VIDEO_NAME = "VIDEO_NAME"
        const val ERROR_MESSAGE = "ERROR_MESSAGE"
    }
}