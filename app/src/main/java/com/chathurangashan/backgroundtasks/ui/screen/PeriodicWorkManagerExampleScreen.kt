package com.chathurangashan.backgroundtasks.ui.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.work.*
import com.chathurangashan.backgroundtasks.R
import com.chathurangashan.backgroundtasks.ThisApplication
import com.chathurangashan.backgroundtasks.database.entities.DeliveryHistory
import com.chathurangashan.backgroundtasks.ui.theme.ErrorInputColor
import com.chathurangashan.backgroundtasks.ui.theme.Typography
import com.chathurangashan.backgroundtasks.workers.DataBackupWorker
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

private const val TAG = "PeriodicWorkManagerExampleScreen"

@Composable
@Preview
fun PeriodicWorkManagerExampleScreen(navController: NavController = rememberNavController()) {

    val context = LocalContext.current
    val applicationContext = context.applicationContext as ThisApplication
    val lifecycleOwner = LocalLifecycleOwner.current
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }

    var addressValue by remember { mutableStateOf("") }
    var addressFiledError by remember { mutableStateOf("") }
    var dropOffTypeFieldValue by remember { mutableStateOf("") }
    var dropOffTypeFieldError by remember { mutableStateOf("") }
    var nicFieldValue by remember { mutableStateOf("") }
    var nicFieldError by remember { mutableStateOf("") }
    var resetDropDown by remember { mutableStateOf(false) }

    fun validateAddress(address: String) {
        addressValue = address
        addressFiledError = if (address.isEmpty()) {
            "address cannot be empty"
        } else {
            ""
        }
    }

    fun validateNIC(nic: String) {
        nicFieldValue = nic
        nicFieldError = if (nic.isEmpty()) {
            "nic cannot be empty"
        } else {
            ""
        }
    }

    fun validateDropOffType(dropOffType: String) {
        dropOffTypeFieldValue = dropOffType
        dropOffTypeFieldError = if (dropOffType.isEmpty()) {
            "drop off value cannot be empty"
        } else {
            ""
        }
    }

    fun onClickSave() {

        validateAddress(addressValue)
        validateNIC(nicFieldValue)
        validateDropOffType(dropOffTypeFieldValue)

        if (addressFiledError.isEmpty() && nicFieldError.isEmpty() && dropOffTypeFieldError.isEmpty()) {

            coroutineScope.launch {

                val deliveryHistory = DeliveryHistory(
                    address = addressValue,
                    nic = nicFieldValue,
                    type = dropOffTypeFieldValue
                )

                applicationContext.database.deliveryHistoryDao().insert(deliveryHistory)
                Toast.makeText(context, "Recode added", Toast.LENGTH_SHORT).show()

                addressValue = ""
                nicFieldValue = ""
                dropOffTypeFieldValue = ""
                resetDropDown = true
                delay(100)
                resetDropDown = false
                focusManager.clearFocus()

            }

        } else {
            Toast.makeText(context, "Please complete the from", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(key1 = Unit) {

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val dataBackupWorkRequest =
            PeriodicWorkRequestBuilder<DataBackupWorker>(
                1, TimeUnit.HOURS, 15, TimeUnit.HOURS)
                .setConstraints(constraints)
                .build()

        val workManager = WorkManager.getInstance(applicationContext)
        workManager.enqueueUniquePeriodicWork(TAG,ExistingPeriodicWorkPolicy.KEEP ,dataBackupWorkRequest)
        workManager.getWorkInfoByIdLiveData(dataBackupWorkRequest.id)
            .observe(lifecycleOwner) { workInfo ->
                when (workInfo.state) {
                    WorkInfo.State.ENQUEUED -> {
                        Log.d(TAG, "data backup worker is started")
                    }
                    WorkInfo.State.RUNNING -> {
                        Log.d(TAG, "data backup worker is running")

                    }
                    WorkInfo.State.SUCCEEDED -> {
                        Log.d(TAG, "data backup worker completed it's work")
                    }
                    WorkInfo.State.FAILED -> {
                        val errorMessage =
                            workInfo.outputData.getString(DataBackupWorker.ERROR_MESSAGE)
                        Log.d(TAG, "data backup worker error: $errorMessage")
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
                text = stringResource(R.string.insert_delivery_recode_title),
                style = Typography.h3,
                textAlign = TextAlign.Center
            )
            TextInputField(
                value = addressValue,
                label = stringResource(R.string.address_edit_text_label),
                isError = addressFiledError.isNotEmpty(),
                onValueChange = { validateAddress(it) },
                errorMessage = addressFiledError,
                focusRequester = focusRequester
            )
            Spacer(
                modifier = Modifier
                    .padding(bottom = 16.dp)
            )
            TextInputField(
                value = nicFieldValue,
                label = stringResource(R.string.nic_text_label),
                isError = nicFieldError.isNotEmpty(),
                onValueChange = { validateNIC(it) },
                errorMessage = nicFieldError,
                focusRequester = focusRequester
            )
            Spacer(
                modifier = Modifier
                    .padding(8.dp)
            )
            DropDownInputField(
                value = dropOffTypeFieldValue,
                label = stringResource(R.string.drop_off_type_label),
                isError = dropOffTypeFieldError.isNotEmpty(),
                onValueChange = { validateDropOffType(it) },
                errorMessage = dropOffTypeFieldError,
                options = listOf("In Person", "Contactless"),
                resetValue = resetDropDown,
                focusRequester = focusRequester
            )
            Spacer(
                modifier = Modifier
                    .padding(16.dp)
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = { onClickSave() },
            ) {
                Text(stringResource(R.string.save_button_text))
            }
        }
    }
}
