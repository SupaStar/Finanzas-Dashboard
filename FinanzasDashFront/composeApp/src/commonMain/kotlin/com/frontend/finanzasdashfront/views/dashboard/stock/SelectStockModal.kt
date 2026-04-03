package com.frontend.finanzasdashfront.views.dashboard.stock

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.frontend.finanzasdashfront.viewmodel.dashboard.stock.SelectStockVM
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectStockModal(
    onClose: () -> Unit,
    reloadData: () -> Unit,
    viewModel: SelectStockVM
) {
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
        launch { viewModel.closeEvent.collect { onClose() } }
        launch { viewModel.reloadDash.collect { reloadData() } }
    }

    AlertDialog(
        onDismissRequest = onClose,
        title = {
            Text(
                "Agregar Acción",
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .heightIn(max = 250.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        state.isLoading -> {
                            CircularProgressIndicator(Modifier.align(Alignment.Center))
                        }

                        state.errorMessage != null -> {
                            ErrorState(state.errorMessage!!) { viewModel.loadStocksAvailable() }
                        }

                        else -> {
                            LazyColumn(modifier = Modifier.fillMaxSize()) {
                                item {
                                    Text(
                                        "Acciones Disponibles",
                                        style = MaterialTheme.typography.labelLarge,
                                        modifier = Modifier.padding(bottom = 8.dp),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                items(state.stocks) { stock ->
                                    ListItem(
                                        headlineContent = { Text(stock.symbol, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface) },
                                        supportingContent = { Text(stock.broker, color = MaterialTheme.colorScheme.onSurfaceVariant) },
                                        trailingContent = {
                                            Text(
                                                "$${stock.closeDay}",
                                                color = MaterialTheme.colorScheme.secondary
                                            )
                                        },
                                        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { viewModel.onStockSelected(stock.stockId) }
                                            .padding(vertical = 4.dp)
                                    )

                                    HorizontalDivider(
                                        thickness = 0.5.dp
                                    )
                                }
                            }
                        }
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("¿No la encuentras? Regístrala", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)

                    OutlinedTextField(
                        value = state.stockName,
                        onValueChange = { viewModel.onStockNameChange(it) },
                        label = { Text("Símbolo (ej: AAPL)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    ExposedDropdownMenuBox(
                        expanded = state.expanded,
                        onExpandedChange = { viewModel.onExpanded(!state.expanded) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = state.selectedOptionText,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Broker") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = state.expanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = state.expanded,
                            onDismissRequest = { viewModel.onExpanded(false) }) {
                            state.brokers.forEach { broker ->
                                DropdownMenuItem(
                                    text = { Text(broker.name) },
                                    onClick = {
                                        viewModel.onBrokerSelected(broker.brokerId, broker.name)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { viewModel.saveStock(state.stockName, state.selectedOptionId) },
                enabled = state.stockName.isNotBlank() && state.selectedOptionId != 0L
            ) {
                Text("Guardar Acción")
            }
        },
        dismissButton = {
            TextButton(onClick = onClose) {
                Text("Cerrar")
            }
        }
    )
}

@Composable
fun ErrorState(error: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(error, color = MaterialTheme.colorScheme.error, textAlign = TextAlign.Center)
        Spacer(Modifier.height(8.dp))
        TextButton(onClick = onRetry) { Text("Reintentar") }
    }
}