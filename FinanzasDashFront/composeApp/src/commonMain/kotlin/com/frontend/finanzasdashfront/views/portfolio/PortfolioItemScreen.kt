package com.frontend.finanzasdashfront.views.portfolio

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.frontend.finanzasdashfront.viewmodel.portfolio.PortfolioViewModel
import com.frontend.finanzasdashfront.viewmodel.portfolio.modal.AddDividendModalVM
import com.frontend.finanzasdashfront.viewmodel.portfolio.modal.AddOperationModalVM
import com.frontend.finanzasdashfront.views.portfolio.modal.AddDividendModal
import com.frontend.finanzasdashfront.views.portfolio.modal.AddOperationModal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortfolioItemScreen(
    onBack: () -> Unit,
    viewModel: PortfolioViewModel,
    addOperationVM: AddOperationModalVM,
    addDividendVm: AddDividendModalVM
) {
    val state by viewModel.uiState.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    var showOperationModal by remember { mutableStateOf(false) }
    var showDividendModal by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                TopAppBar(
                    title = { Text("Acción: ${state.stockName}") },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, "Volver")
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
                onAddOperation = { showOperationModal = true; expanded = false },
                onAddDividend = { showDividendModal = true; expanded = false }
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
            Box(modifier = Modifier.padding(paddingValues)) {
                if (state.isLoading) {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                } else {
                    Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                        if (state.isLoading) {
                            CircularProgressIndicator(Modifier.align(Alignment.Center))
                        } else {
                            // Alternamos el contenido según la pestaña seleccionada
                            when (state.selectedTabIndex) {
                                0 -> OperationTab(state.operations)
                                1 -> DividendTab(state.dividends)
                                2 -> InfoTab(uiState = state, viewModel::onYearSelectedChanged)
                            }
                        }
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
                }
            }
        }
    }
}

