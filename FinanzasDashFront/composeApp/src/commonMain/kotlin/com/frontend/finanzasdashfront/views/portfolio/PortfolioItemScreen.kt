package com.frontend.finanzasdashfront.views.portfolio

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.frontend.finanzasdashfront.dto.dividend.DividendDto
import com.frontend.finanzasdashfront.dto.enums.OperationTypeEnum
import com.frontend.finanzasdashfront.dto.operation.OperationDto
import com.frontend.finanzasdashfront.viewmodel.portfolio.FibraDividendCalculatorVM
import com.frontend.finanzasdashfront.viewmodel.portfolio.PortfolioViewModel
import com.frontend.finanzasdashfront.viewmodel.portfolio.modal.AddDividendModalVM
import com.frontend.finanzasdashfront.viewmodel.portfolio.modal.AddOperationModalVM
import com.frontend.finanzasdashfront.views.portfolio.modal.AddDividendModal
import com.frontend.finanzasdashfront.views.portfolio.modal.AddOperationModal
import com.frontend.finanzasdashfront.views.portfolio.modal.FibraDividendCalculatorModal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortfolioItemScreen(
    onBack: () -> Unit,
    viewModel: PortfolioViewModel,
    addOperationVM: AddOperationModalVM,
    addDividendVm: AddDividendModalVM,
    fibraCalculatorVM: FibraDividendCalculatorVM
) {
    val state by viewModel.uiState.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    var showOperationModal by remember { mutableStateOf(false) }
    var showDividendModal by remember { mutableStateOf(false) }
    var showCalculatorModal by remember { mutableStateOf(false) }

    var operationToDelete by remember { mutableStateOf<OperationDto?>(null) }
    var dividendToDelete by remember { mutableStateOf<DividendDto?>(null) }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                TopAppBar(
                    title = { Text("Acción: ${state.stockName}") },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, "Volver")
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            val titulos = state.operations
                                .sumOf { op ->
                                    val qty = op.quantity.toDouble()
                                    if (op.operationType == OperationTypeEnum.buy) qty else -qty
                                }.toFloat()
                            fibraCalculatorVM.init(titulos)
                            showCalculatorModal = true
                        }) {
                            Icon(Icons.Default.Calculate, contentDescription = "Calculadora FIBRA")
                        }
                    }
                )
                TabRow(selectedTabIndex = state.selectedTabIndex) {
                    state.optionsTabs.forEachIndexed { index, title ->
                        Tab(
                            selected = state.selectedTabIndex == index,
                            onClick = { viewModel.onTabIndexChanged(index) },
                            text = {
                                Text(title, style = MaterialTheme.typography.titleSmall)
                            }
                        )
                    }
                }
            }

        },
        floatingActionButton = {
            ExpandableFabMenu(
                expanded = expanded,
                onExpandClick = { expanded = !expanded },
                onAddOperation = { 
                    addOperationVM.initModal(state.stockCurrency == "USD")
                    showOperationModal = true
                    expanded = false 
                },
                onAddDividend = { 
                    addDividendVm.initModal()
                    showDividendModal = true
                    expanded = false 
                }
            )
        }
    ) { paddingValues ->
        if (state.isLoading) {
            Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (state.errorMessage != null) {
            Text(text = state.errorMessage!!, modifier = Modifier.padding(paddingValues))
        } else {
            Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                if (state.isLoading) {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                } else {
                    when (state.selectedTabIndex) {
                        0 -> OperationTab(
                            operations = state.operations,
                            onEdit = {
                                addOperationVM.initModalForEdit(it, state.stockCurrency == "USD")
                                showOperationModal = true
                            },
                            onDelete = { operationToDelete = it }
                        )
                        1 -> DividendTab(
                            dividends = state.dividends,
                            onEdit = {
                                addDividendVm.initModalForEdit(it)
                                showDividendModal = true
                            },
                            onDelete = { dividendToDelete = it }
                        )
                        2 -> InfoTab(uiState = state, viewModel::onYearSelectedChanged)
                    }
                }
            }

            if (showCalculatorModal) {
                FibraDividendCalculatorModal(
                    viewModel = fibraCalculatorVM,
                    onClose = { showCalculatorModal = false }
                )
            }

            if (showOperationModal) {
                AddOperationModal(
                    viewModel = addOperationVM, onClose = { showOperationModal = false },
                    reloadOperations = { viewModel.loadPortfolioData() }, idPorfolio = state.portfolioid
                )
            }
            if (showDividendModal) {
                AddDividendModal(
                    viewModel = addDividendVm, onClose = { showDividendModal = false },
                    reloadDividends = { viewModel.loadPortfolioData() }, idPorfolio = state.portfolioid
                )
            }
            
            operationToDelete?.let { operation ->
                AlertDialog(
                    onDismissRequest = { operationToDelete = null },
                    title = { Text("Eliminar Operación") },
                    text = { Text("¿Estás seguro de que deseas eliminar esta operación?") },
                    confirmButton = {
                        TextButton(onClick = {
                            operation.operationId?.let { viewModel.deleteOperation(it) }
                            operationToDelete = null
                        }) {
                            Text("Eliminar")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { operationToDelete = null }) {
                            Text("Cancelar")
                        }
                    }
                )
            }

            dividendToDelete?.let { dividend ->
                AlertDialog(
                    onDismissRequest = { dividendToDelete = null },
                    title = { Text("Eliminar Dividendo") },
                    text = { Text("¿Estás seguro de que deseas eliminar este dividendo?") },
                    confirmButton = {
                        TextButton(onClick = {
                            dividend.dividendId?.let { viewModel.deleteDividend(it) }
                            dividendToDelete = null
                        }) {
                            Text("Eliminar")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { dividendToDelete = null }) {
                            Text("Cancelar")
                        }
                    }
                )
            }
        }
    }
}

