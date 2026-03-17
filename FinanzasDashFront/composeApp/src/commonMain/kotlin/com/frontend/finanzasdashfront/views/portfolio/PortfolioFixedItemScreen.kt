package com.frontend.finanzasdashfront.views.portfolio

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.frontend.finanzasdashfront.viewmodel.portfolio.PortfolioFixedItemVM
import com.frontend.finanzasdashfront.utils.formatCurrency
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.frontend.finanzasdashfront.enum.FixedPortfolioOperationTypeEnum
import com.frontend.finanzasdashfront.dto.request.AddFixedPortfolioOperationDto
import com.frontend.finanzasdashfront.views.portfolio.modal.FixedPortfolioOperationModal
import com.frontend.finanzasdashfront.views.portfolio.modal.EditFixedPortfolioAmountModal
import androidx.compose.material.icons.filled.Edit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortfolioFixedItemScreen(
    onBack: () -> Unit,
    viewModel: PortfolioFixedItemVM
) {
    val state by viewModel.uiState.collectAsState()
    
    var showOperationModal by remember { mutableStateOf(false) }
    var showEditAmountModal by remember { mutableStateOf(false) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var operationType by remember { mutableStateOf(FixedPortfolioOperationTypeEnum.deposit) }

    LaunchedEffect(state.deleteSuccess) {
        if (state.deleteSuccess) {
            onBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = if (state.portfolio != null) 
                            "Renta Fija: ${state.portfolio!!.fixedInstrument.name}" 
                        else "Cargando..."
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { showEditAmountModal = true }) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar monto")
                    }
                    IconButton(onClick = { showDeleteConfirmDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar portafolio", tint = MaterialTheme.colorScheme.error)
                    }
                }
            )
        },
        floatingActionButton = {
            if (state.portfolio != null) {
                var expanded by remember { mutableStateOf(false) }
                Column(horizontalAlignment = Alignment.End) {
                    if (expanded) {
                        SmallFloatingActionButton(
                            onClick = {
                                operationType = FixedPortfolioOperationTypeEnum.deposit
                                showOperationModal = true
                                expanded = false
                            },
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            Icon(Icons.Default.KeyboardArrowUp, "Abonar")
                        }
                        SmallFloatingActionButton(
                            onClick = {
                                operationType = FixedPortfolioOperationTypeEnum.withdrawal
                                showOperationModal = true
                                expanded = false
                            },
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            Icon(Icons.Default.KeyboardArrowDown, "Retirar")
                        }
                    }
                    FloatingActionButton(onClick = { expanded = !expanded }) {
                        Icon(if (expanded) Icons.Default.Add else Icons.Default.Add, "Operaciones") // Using Add for both for simplicity, or could use different icon when expanded
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (state.errorMessage != null) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = state.errorMessage!!, color = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.loadData() }) {
                        Text("Reintentar")
                    }
                }
            } else if (state.portfolio != null) {
                // Sort newest first
                val sortedPays = state.dailyPays.sortedByDescending { it.payDate }
                val totalInterest = sortedPays.sumOf { it.amount }

                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    // ── Monto Total ──────────────────────────────────────
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Monto Total Actual",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Text(
                                    text = "$${state.portfolio!!.amount.toFloat().formatCurrency()}",
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Tasa Anual: ${state.portfolio!!.fixedInstrument.anualRate}%",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }

                    // ── Interés Generado ─────────────────────────────────
                    if (sortedPays.isNotEmpty()) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                                )
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "Interés Generado",
                                            style = MaterialTheme.typography.labelLarge,
                                            color = MaterialTheme.colorScheme.onTertiaryContainer
                                        )
                                        Text(
                                            text = "${sortedPays.size} pago(s) acumulado(s)",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f)
                                        )
                                    }
                                    Text(
                                        text = "+$${totalInterest.toFloat().formatCurrency()}",
                                        style = MaterialTheme.typography.headlineSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onTertiaryContainer
                                    )
                                }
                            }
                        }
                    }

                    // ── Section header ─────────────────────────────────────
                    item {
                        Text(
                            text = "Historial de Rendimientos Diarios",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                        )
                    }

                    // ── Empty state ─────────────────────────────────────
                    if (sortedPays.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth().height(120.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "No hay rendimientos generados aún.",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    // ── Daily pay cards ──────────────────────────────────
                    items(sortedPays) { dailyPay ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = if (dailyPay.payDate.isNotBlank()) dailyPay.payDate else "Sin fecha",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "Tasa: ${dailyPay.anualRateCalculated}%",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Text(
                                    text = "+$${dailyPay.amount.toFloat().formatCurrency()}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    // ── Operations Section ─────────────────────────────────────
                    if (state.operations.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Historial de Abonos y Retiros",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                            )
                        }

                        items(state.operations) { op ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = if (op.operationType == FixedPortfolioOperationTypeEnum.deposit) "Abono" else "Retiro",
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.SemiBold,
                                            color = if (op.operationType == FixedPortfolioOperationTypeEnum.deposit) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                                        )
                                        Text(
                                            text = op.operationDate,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Text(
                                        text = "${if (op.operationType == FixedPortfolioOperationTypeEnum.deposit) "+" else "-"}$${op.amount.formatCurrency()}",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = if (op.operationType == FixedPortfolioOperationTypeEnum.deposit) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }

                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
        
        if (showOperationModal) {
            FixedPortfolioOperationModal(
                onDismissRequest = { showOperationModal = false },
                onConfirm = { amount, type ->
                    viewModel.addOperation(AddFixedPortfolioOperationDto(amount, type))
                    showOperationModal = false
                },
                operationType = operationType
            )
        }

        if (showEditAmountModal && state.portfolio != null) {
            EditFixedPortfolioAmountModal(
                initialAmount = state.portfolio!!.amount.toFloat(),
                onDismissRequest = { showEditAmountModal = false },
                onConfirm = { amount ->
                    viewModel.updateAmount(amount)
                    showEditAmountModal = false
                }
            )
        }

        if (showDeleteConfirmDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirmDialog = false },
                title = { Text("Eliminar Portafolio") },
                text = { Text("¿Estás seguro de que deseas eliminar este portafolio de Renta Fija? Esta acción no se puede deshacer.") },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.deletePortfolio()
                            showDeleteConfirmDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Eliminar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteConfirmDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}
