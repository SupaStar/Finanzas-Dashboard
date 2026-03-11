package com.frontend.finanzasdashfront.views.portfolio.modal


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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

    LaunchedEffect(Unit) {
        launch { viewModel.closeEvent.collect { onClose() } }
        launch { viewModel.reloadOperationsEvent.collect { reloadOperations() } }
    }

    Dialog(onDismissRequest = onClose) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 3.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        if (state.isEditMode) "Editar Operación" else "Nueva Operación",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    IconButton(onClick = onClose) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (state.isLoading) {
                    Box(Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    if (state.errorMessage.isNotBlank()) {
                        Text(
                            text = state.errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    ExposedDropdownMenuBox(
                        expanded = state.isExpandedSelect,
                        onExpandedChange = { viewModel.onExpandedSelectOperationType(!state.isExpandedSelect) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = state.operationTypeSelectedText,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Tipo de operación") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = state.isExpandedSelect) },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = state.isExpandedSelect,
                            onDismissRequest = { viewModel.onExpandedSelectOperationType(false) }
                        ) {
                            EnumOperationTypeDropdown.entries.forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type.label) },
                                    onClick = {
                                        viewModel.onOperationTypeSelected(type.label, type.name.lowercase())
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Grid de Valores (2x2 para mejor usabilidad)
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OperationField(
                            label = "Cantidad",
                            value = state.quantity,
                            onValueChange = viewModel::onQuantityChange,
                            modifier = Modifier.weight(1f)
                        )
                        OperationField(
                            label = "Precio",
                            value = state.price,
                            onValueChange = viewModel::onPriceChange,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OperationField(
                            label = "Comisión",
                            value = state.fee,
                            onValueChange = viewModel::onFeeChange,
                            modifier = Modifier.weight(1f),
                            enabled = !state.isUsd
                        )
                        OperationField(
                            label = "Impuestos",
                            value = state.tax,
                            onValueChange = viewModel::onTaxChange,
                            modifier = Modifier.weight(1f),
                            enabled = !state.isUsd
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    DatePickerField(
                        selectedDate = state.operationDate,
                        onDateSelected = { viewModel.onDateChange(it) }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { viewModel.saveOperation(idPortfolio = idPorfolio) },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        // enabled = state.isFormValid // Descomenta cuando valides en el VM
                    ) {
                        Text(if (state.isEditMode) "Actualizar Transacción" else "Guardar Transacción", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }
    }
}

/**
 * Componente interno para evitar repetición de código en los textfields
 */
@Composable
fun OperationField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, style = MaterialTheme.typography.bodySmall) },
        modifier = modifier,
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        enabled = enabled
    )
}