package com.chathurangashan.backgroundtasks.ui.screen

import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chathurangashan.backgroundtasks.ui.theme.ErrorInputColor
import com.chathurangashan.backgroundtasks.ui.theme.Typography
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Preview(name = "Text input design", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun TextInputField(
    label: String = "Email",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    value: String = "",
    onValueChange: (String) -> Unit = {},
    isError: Boolean = true,
    errorMessage: String = "Invalid input",
    focusRequester: FocusRequester = FocusRequester(),
    readOnly: Boolean = true,
) {

    Column {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            singleLine = true,
            readOnly = readOnly,
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation,
            isError = isError,
        )
        if (isError) {
            Text(
                modifier = Modifier
                    .padding(start = 16.dp),
                text = errorMessage,
                color = ErrorInputColor,
                style = Typography.h6
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview(name = "Drop down design", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun DropDownInputField(
    label: String = "Options",
    value: String = "",
    onValueChange: (String) -> Unit = {},
    isError: Boolean = true,
    resetValue: Boolean = true,
    errorMessage: String = "Please select an option",
    options: List<String> = listOf("Option 1", "Option 2", "Option 3", "Option 4", "Option 5"),
    focusRequester: FocusRequester = FocusRequester()
) {

    var selectedValue by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    if (resetValue) {
        selectedValue = ""
    }

    Column {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                readOnly = true,
                value = selectedValue,
                onValueChange = onValueChange,
                label = { Text(label) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                isError = isError,
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {
                options.forEach { selectionOption ->
                    DropdownMenuItem(
                        onClick = {
                            selectedValue = selectionOption
                            onValueChange(selectedValue)
                            expanded = false
                        }
                    ) {
                        Text(text = selectionOption)
                    }
                }
            }
        }
        if (isError) {
            Text(
                modifier = Modifier
                    .padding(start = 16.dp),
                text = errorMessage,
                color = ErrorInputColor,
                style = Typography.h6
            )
        }
    }
}

@Composable
fun TimePicker(
    value: String,
    label: String = "Options",
    isError: Boolean = true,
    errorMessage: String = "Invalid input",
    onValueChange: (String) -> Unit = {},
    focusRequester: FocusRequester = FocusRequester(),
    pattern: String = "HH:mm",
    is24HourView: Boolean = true,
) {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    val time = if (value.isNotBlank()) LocalTime.parse(value, formatter) else LocalTime.now()
    val dialog = TimePickerDialog(
        LocalContext.current,
        { _, hour, minute -> onValueChange(LocalTime.of(hour, minute).toString()) },
        time.hour,
        time.minute,
        is24HourView,
    )

    Column(
        modifier = Modifier
            .clickable { dialog.show() }
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            singleLine = true,
            readOnly = false,
            keyboardOptions = KeyboardOptions.Default,
            visualTransformation = VisualTransformation.None,
            isError = isError,
        )
        if (isError) {
            Text(
                modifier = Modifier
                    .padding(start = 16.dp),
                text = errorMessage,
                color = ErrorInputColor,
                style = Typography.h6
            )
        }
    }
}