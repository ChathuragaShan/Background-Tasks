package com.chathurangashan.backgroundtasks.data.general

sealed class DownloadState{
    data class Downloading(val progress: Int) : DownloadState()
    object Finished : DownloadState()
    data class Failed(val error: Throwable? = null) : DownloadState()
}
