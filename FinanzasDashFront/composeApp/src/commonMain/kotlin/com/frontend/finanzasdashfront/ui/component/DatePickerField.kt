package com.frontend.finanzasdashfront.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.frontend.finanzasdashfront.utils.formatMillisToDateString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(
    selectedDate: String,
    onDateSelected: (String) -> Unit
) {
    val datePickerState = rememberDatePickerState()
    var showSheet by remember { mutableStateOf(false) }
    val dateFormatter = remember { DatePickerDefaults.dateFormatter() }

    val selectedMillis = datePickerState.selectedDateMillis
    LaunchedEffect(selectedMillis) {
        selectedMillis?.let {
            onDateSelected(formatMillisToDateString(selectedMillis))
            showSheet = false
        }
    }

    OutlinedTextField(
        value = selectedDate,
        onValueChange = { },
        readOnly = true,
        label = { Text("Fecha de operación") },
        trailingIcon = {
            // CORRECCIÓN: Simplemente cambia el valor de showSheet
            IconButton(onClick = { showSheet = true }) {
                Icon(Icons.Default.DateRange, contentDescription = "Seleccionar fecha")
            }
        },
        modifier = Modifier.fillMaxWidth()
    )

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            ) {
                DatePicker(
                    state = datePickerState,
                    showModeToggle = false,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}