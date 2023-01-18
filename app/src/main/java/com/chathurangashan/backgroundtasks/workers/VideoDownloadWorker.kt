package com.chathurangashan.backgroundtasks.workers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Environment
import androidx.core.app.NotificationCompat
import androidx.work.*
import com.chathurangashan.backgroundtasks.R
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

private const val VIDEO_DOWNLOAD_NOTIFICATION_ID = 3
private const val CHANNEL_ID = "video_download_notification"
class VideoDownloadWorker (appContext: Context, workerParams: WorkerParameters): CoroutineWorker(appContext, workerParams)  {

    private val thisApplication = appContext as ThisApplication
    private lateinit var fileDownloadState: Result
    private lateinit var notification: Notification
    private val notificationManager by lazy { thisApplication.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }

    override suspend fun doWork(): Result = coroutineScope {

        val networkService by lazy { thisApplication.networkService }
        val videoFileName =
            inputData.getString(VIDEO_NAME) ?: return@coroutineScope Result.failure()
        downloadVideo(networkService,videoFileName)

        return@coroutineScope fileDownloadState

    }

    private suspend fun downloadVideo (networkService: ApiService, videoFileName: String) {

        try {

            val filePath = File(
                thisApplication.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
                File.separator.toString() + "$videoFileName.mp4"
            ).toString()

            setForeground(createForegroundInfo())

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

    private fun createForegroundInfo(): ForegroundInfo {

        val intent = WorkManager.getInstance(applicationContext).createCancelPendingIntent(id)

        notification = NotificationCompat.Builder(thisApplication, CHANNEL_ID)
            .setContentTitle(thisApplication.getText(R.string.video_download_notification_title))
            .setContentText(thisApplication.getText(R.string.video_download_description))
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_stat_file_download)
            .addAction(android.R.drawable.ic_delete, thisApplication.getText(R.string.video_download_cancel), intent)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                "Video Download Notification",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationChannel.description = "This is video download notification"
            notificationManager.createNotificationChannel(notificationChannel)
            notificationManager.notify(VIDEO_DOWNLOAD_NOTIFICATION_ID, notification)
        }

        return ForegroundInfo(VIDEO_DOWNLOAD_NOTIFICATION_ID, notification)
    }

    companion object {
        const val DOWNLOAD_PROGRESS = "DOWNLOAD_PROGRESS"
        const val DOWNLOADED_FILE_NAME = "DOWNLOADED_FILE_NAME"
        const val VIDEO_NAME = "VIDEO_NAME"
        const val ERROR_MESSAGE = "ERROR_MESSAGE"
    }
}