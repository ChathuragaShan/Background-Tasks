package com.chathurangashan.backgroundtasks.ui.screen

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.chathurangashan.backgroundtasks.broadcastreceiver.AlarmManagerTrigger
import com.chathurangashan.backgroundtasks.R
import com.chathurangashan.backgroundtasks.ui.theme.ErrorInputColor
import com.chathurangashan.backgroundtasks.ui.theme.Typography
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
@Preview
fun AlarmManagerExampleScreen(navController: NavController = rememberNavController()) {

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    var selectedPickupTimeFieldValue by remember { mutableStateOf(LocalDateTime.now()) }
    var timePickerFieldValue by remember { mutableStateOf("") }
    val timePickerError by remember { mutableStateOf("") }

    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    val time = if (timePickerFieldValue.isNotBlank()) LocalTime.parse(
        timePickerFieldValue,
        formatter
    ) else LocalTime.now()
    val dialog = TimePickerDialog(
        LocalContext.current,
        { _, hour, minute ->
            val selectedLocalTime = LocalTime.of(hour, minute)
            val today: LocalDate = LocalDate.now()
            selectedPickupTimeFieldValue = selectedLocalTime.atDate(today)
            timePickerFieldValue = LocalTime.of(hour, minute).toString()
        },
        time.hour,
        time.minute,
        true,
    )

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
                text = stringResource(R.string.alarm_manager_screen_title_text),
                style = Typography.h1,
                textAlign = TextAlign.Center
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                Column(
                    modifier = Modifier
                        .weight(0.60F)
                ) {
                    TextField(
                        modifier = Modifier
                            .focusRequester(focusRequester),
                        value = timePickerFieldValue,
                        onValueChange = { timePickerFieldValue = it },
                        label = { Text(stringResource(R.string.time_picker_hint)) },
                        singleLine = true,
                        readOnly = true,
                        isError = timePickerError.isNotEmpty(),
                    )
                    if (timePickerError.isNotEmpty()) {
                        Text(
                            modifier = Modifier
                                .padding(start = 16.dp),
                            text = timePickerError,
                            color = ErrorInputColor,
                            style = Typography.h6
                        )
                    }
                }
                Button(
                    modifier = Modifier
                        .weight(0.30F)
                        .height(55.dp),
                    onClick = { dialog.show() },
                ) {
                    Text(stringResource(R.string.open_time_picker_button_text))
                }
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = { onClickSave(context, selectedPickupTimeFieldValue) },
            ) {
                Text(stringResource(R.string.save_button_text))
            }
        }
    }
}

fun onClickSave(context: Context, selectedLocalDateTime: LocalDateTime) {

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, AlarmManagerTrigger::class.java)
    val pending = PendingIntent.getBroadcast(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val milliseconds = selectedLocalDateTime.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000
    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, milliseconds, pending)
}


