package com.chathurangashan.backgroundtasks.ui.screen

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.chathurangashan.backgroundtasks.R
import com.chathurangashan.backgroundtasks.ui.theme.Typography
import com.chathurangashan.backgroundtasks.workers.ImageDownloadWorker

private const val TAG = "LongRunningWorkManagerExampleScreen"

@Composable
@Preview
fun LongRunningWorkManagerExampleScreen(navController: NavController = rememberNavController()) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var fileDownloadProgress by remember { mutableStateOf(0.0f) }
    var isFileDownloadOCompleted by remember { mutableStateOf(false) }
    val animatedProgress = animateFloatAsState(
        targetValue = fileDownloadProgress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    ).value

    LaunchedEffect(key1 = Unit) {

        val uploadWorkRequest = OneTimeWorkRequestBuilder<ImageDownloadWorker>()
            .setInputData(
                workDataOf(
                    ImageDownloadWorker.IMAGE_NAME to "short_film"
                )
            )
            .build()

        val workManager = WorkManager.getInstance(context.applicationContext)
        workManager.enqueue(uploadWorkRequest)
        workManager.getWorkInfoByIdLiveData(uploadWorkRequest.id)
            .observe(lifecycleOwner) { workInfo ->
                when (workInfo.state) {
                    WorkInfo.State.ENQUEUED -> {
                        Log.d(TAG, "file download worker is started")
                    }
                    WorkInfo.State.RUNNING -> {

                        val progress = workInfo.progress
                            .getInt(ImageDownloadWorker.DOWNLOAD_PROGRESS, 0)

                        fileDownloadProgress = progress / 100f

                        Log.d(TAG, "file download progress: $progress")

                    }
                    WorkInfo.State.SUCCEEDED -> {

                        val downloadFile =
                            workInfo.outputData.getString(ImageDownloadWorker.DOWNLOADED_FILE_NAME)

                        fileDownloadProgress = 1.0f
                        isFileDownloadOCompleted = true

                        Log.d(TAG, "downloaded file name: $downloadFile")
                    }
                    WorkInfo.State.FAILED -> {

                        val errorMessage =
                            workInfo.outputData.getString(ImageDownloadWorker.ERROR_MESSAGE)
                        Log.d(TAG, "downloaded error: $errorMessage")

                    }
                    else -> {
                        Log.d(TAG, "other work state: ${workInfo.state}")

                    }
                }
            }
    }

    Box(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(26.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .padding(bottom = 16.dp),
                text = stringResource(R.string.work_manager_video_download_button_text),
                style = Typography.h1,
                textAlign = TextAlign.Center
            )
            LinearProgressIndicator(
                modifier = Modifier
                    .padding(8.dp),
                progress = animatedProgress,
            )
            if (isFileDownloadOCompleted) {
                Text(
                    modifier = Modifier
                        .padding(bottom = 8.dp),
                    text = stringResource(R.string.video_download_success_message),
                    style = Typography.body2,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}