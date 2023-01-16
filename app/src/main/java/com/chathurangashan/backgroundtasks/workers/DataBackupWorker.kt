package com.chathurangashan.backgroundtasks.workers

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.chathurangashan.backgroundtasks.ThisApplication
import com.chathurangashan.backgroundtasks.data.moshi.DeliveryRecodeRequest
import com.chathurangashan.backgroundtasks.database.entities.DeliveryHistory
import com.chathurangashan.backgroundtasks.network.ConnectivityInterceptor
import kotlinx.coroutines.coroutineScope
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

class DataBackupWorker(appContext: Context, workerParams: WorkerParameters): CoroutineWorker(appContext, workerParams) {

    private val thisApplication = appContext as ThisApplication

    override suspend fun doWork(): Result = coroutineScope {

        val networkService by lazy { thisApplication.networkService }
        val database by lazy { thisApplication.database }

        val allUnSync = database.deliveryHistoryDao().getAllUnSync()

        if(allUnSync.isNotEmpty()){

            val deliveryRecodeList = mutableListOf<DeliveryRecodeRequest>()

            allUnSync.forEach {

                val deliveryRecodeRequest = DeliveryRecodeRequest(
                    id = it.id,
                    address = it.address,
                    nic = it.nic,
                    deliveryType = it.type
                )

                deliveryRecodeList.add(deliveryRecodeRequest)
            }

            try {

                val updateDeliveryRequestData =
                    networkService.updateDeliveryRequestData(deliveryRecodeList)

                if(updateDeliveryRequestData.status){

                    database.deliveryHistoryDao().updateSyncStatus()
                    return@coroutineScope Result.success()
                }else{
                    return@coroutineScope Result.failure(
                        workDataOf(ImageDownloadWorker.ERROR_MESSAGE to updateDeliveryRequestData.message))
                }

            }catch (exception: Exception){
                return@coroutineScope when (exception) {
                    is HttpException ->
                        Result.failure(workDataOf(ERROR_MESSAGE to "Network Error"))
                    is ConnectivityInterceptor.NoConnectivityException ->
                        Result.failure(workDataOf(ERROR_MESSAGE to "No Connection Error"))
                    is SocketTimeoutException ->
                        Result.retry()
                    is IOException ->
                        Result.failure(workDataOf(ERROR_MESSAGE to "Processing Error"))
                    else ->
                        Result.failure(workDataOf(ERROR_MESSAGE to "UnKnown Error"))
                }

            }

        }

        return@coroutineScope Result.success()
    }

    companion object {
        const val ERROR_MESSAGE = "ERROR_MESSAGE"
    }
}