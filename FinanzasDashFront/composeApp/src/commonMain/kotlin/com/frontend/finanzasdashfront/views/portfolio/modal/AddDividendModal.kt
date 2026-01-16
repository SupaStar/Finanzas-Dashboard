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
import com.frontend.finanzasdashfront.model.portfolio.modal.EnumCurrencyCodesDropdown
import com.frontend.finanzasdashfront.model.portfolio.modal.EnumDividendTypeDropdown
import com.frontend.finanzasdashfront.ui.component.DatePickerField
import com.frontend.finanzasdashfront.ui.component.GenericExposedDropdown
import com.frontend.finanzasdashfront.viewmodel.portfolio.modal.AddDividendModalVM
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDividendModal(
    viewModel: AddDividendModalVM, onClose: () -> Unit,
    reloadDividends: () -> Unit,
    idPorfolio: Long
) {

    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        launch { viewModel.closeModalEvent.collect { onClose() } }
        launch { viewModel.reloadDividendsEvent.collect { reloadDividends() } }
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
                        "Nueva Dividendo",
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
                        expanded = state.isExpandedDividendTypeSelected,
                        onExpandedChange = { viewModel.onExpandedDividendTypeChange(!state.isExpandedDividendTypeSelected) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = state.dividendTypeSelectedText,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Tipo de operación") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = state.isExpandedDividendTypeSelected) },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = state.isExpandedDividendTypeSelected,
                            onDismissRequest = { viewModel.onExpandedDividendTypeChange(false) }
                        ) {
                            EnumDividendTypeDropdown.entries.forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type.label) },
                                    onClick = {
                                        viewModel.onDividendTypeChange(type.label, type.name.lowercase())
                                    }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        DividendField(
                            label = "Valor",
                            value = state.value,
                            onValueChange = viewModel::onValueChange,
                            modifier = Modifier.weight(1f),
                        )

                        ExposedDropdownMenuBox(
                            expanded = state.isExpandedCurrencyCodeSelected,
                            onExpandedChange = { viewModel.onExpandedCurrencyChange(!state.isExpandedCurrencyCodeSelected) },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = state.currencyCodeSelectedText,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Moneda") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = state.isExpandedCurrencyCodeSelected) },
                                modifier = Modifier.menuAnchor().fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )
                            ExposedDropdownMenu(
                                expanded = state.isExpandedCurrencyCodeSelected,
                                onDismissRequest = { viewModel.onExpandedDividendTypeChange(false) }
                            ) {
                                EnumCurrencyCodesDropdown.entries.forEach { type ->
                                    DropdownMenuItem(
                                        text = { Text(type.label) },
                                        onClick = {
                                            viewModel.onCurrencyTypeChange(type.label, type.name.uppercase())
                                        }
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    DatePickerField(
                        selectedDate = state.paidDateSelected,
                        onDateSelected = { viewModel.onDateChange(it) }
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        DividendField(
                            label = "Impuesto",
                            value = state.tax,
                            onValueChange = viewModel::onTaxChanged,
                            modifier = Modifier.weight(1f),
                        )
                        DividendField(
                            label = "Tipo de cambio",
                            value = state.exchangeRate,
                            onValueChange = viewModel::onExchangeRateChanged,
                            modifier = Modifier.weight(1f),
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { viewModel.saveDividend(idPortfolio = idPorfolio) },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                    ) {
                        Text("Guardar dividendo", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }
    }
}

@Composable
fun DividendField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, style = MaterialTheme.typography.bodySmall) },
        modifier = modifier,
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
    )
}