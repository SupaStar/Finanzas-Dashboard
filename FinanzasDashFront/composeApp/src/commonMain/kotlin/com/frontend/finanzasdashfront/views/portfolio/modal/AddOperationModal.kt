package com.frontend.finanzasdashfront.views.portfolio.modal


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.frontend.finanzasdashfront.model.portfolio.modal.EnumOperationTypeDropdown
import com.frontend.finanzasdashfront.ui.component.DatePickerField
import com.frontend.finanzasdashfront.viewmodel.portfolio.modal.AddOperationModalVM
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOperationModal(
    viewModel: AddOperationModalVM,
    onClose: () -> Unit,
    reloadOperations: () -> Unit,
    idPorfolio: Long
) {
    val state by viewModel.uiState.collectAsState()
//    val fields = listOf(
//        state.operationTypeValue,
//        state.quantity,
//        state.price,
//        state.fee,
//        state.tax,
//        state.operationDate
//    )
//    val isFormValid = fields.all { it.isNotBlank() }

    LaunchedEffect(Unit){
        launch { viewModel.closeEvent.collect { onClose() } }
        launch { viewModel.reloadOperationsEvent.collect { reloadOperations()} }
    }
    Dialog(onDismissRequest = onClose) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f),
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Agregar Operacion",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    IconButton(onClick = onClose) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar")
                    }
                }
                HorizontalDivider()
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        state.isLoading -> {
                            CircularProgressIndicator(Modifier.align(Alignment.Center))
                        }

                        else -> {
                            if (state.errorMessage != "") {
                                Text(state.errorMessage)
                            }
                            Column {
                                ExposedDropdownMenuBox(
                                    expanded = state.isExpandedSelect,
                                    onExpandedChange = { viewModel.onExpandedSelectOperationType(newValue = !state.isExpandedSelect) },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    OutlinedTextField(
                                        value = state.operationTypeSelectedText,
                                        onValueChange = {},
                                        readOnly = true,
                                        label = { Text("Tipo de operacion") },
                                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = state.isExpandedSelect) },
                                        modifier = Modifier.menuAnchor().fillMaxWidth()
                                    )
                                    ExposedDropdownMenu(
                                        expanded = state.isExpandedSelect,
                                        onDismissRequest = { viewModel.onExpandedSelectOperationType(false) }) {
                                        EnumOperationTypeDropdown.entries.forEach { type ->
                                            DropdownMenuItem(
                                                text = { Text(type.label) },
                                                onClick = {
                                                    viewModel.onOperationTypeSelected(
                                                        operationType = type.label,
                                                        operationValue = type.name.lowercase()
                                                    )
                                                }
                                            )
                                        }
                                    }
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedTextField(
                                        value = state.quantity,
                                        onValueChange = { viewModel.onQuantityChange(it) },
                                        label = { Text("Cantidad") },
                                        modifier = Modifier.weight(1f),
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                    )
                                    OutlinedTextField(
                                        value = state.price,
                                        onValueChange = { viewModel.onPriceChange(it) },
                                        label = { Text("Costo") },
                                        modifier = Modifier.weight(1f),
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                    )
                                    OutlinedTextField(
                                        value = state.fee,
                                        onValueChange = { viewModel.onFeeChange(it) },
                                        label = { Text("Comision") },
                                        modifier = Modifier.weight(1f),
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                    )
                                    OutlinedTextField(
                                        value = state.tax,
                                        onValueChange = { viewModel.onTaxChange(it) },
                                        label = { Text("Impuestos") },
                                        modifier = Modifier.weight(1f),
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                    )
                                }
                                DatePickerField(
                                    selectedDate = state.operationDate,
                                    onDateSelected = { newDateString ->
                                        viewModel.onDateChange(newDateString)
                                    }
                                )
                                Button(
                                    onClick = { viewModel.saveOperation(idPortfolio = idPorfolio) },
//                                    enabled = !isFormValid
                                ) {
                                    Text("Guardar")
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}