package com.frontend.finanzasdashfront.views.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.frontend.finanzasdashfront.viewmodel.dashboard.ChangePasswordVM
import com.frontend.finanzasdashfront.viewmodel.dashboard.DashboardViewModel
import com.frontend.finanzasdashfront.viewmodel.dashboard.stock.SelectStockVM
import com.frontend.finanzasdashfront.views.dashboard.stock.SelectStockModal
import com.frontend.finanzasdashfront.viewmodel.portfolio.modal.AddFixedPortfolioModalVM
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi

@OptIn(ExperimentalMaterial3Api::class, ExperimentalKoalaPlotApi::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    viewModelModal: SelectStockVM,
    addFixedPortfolioModalVM: AddFixedPortfolioModalVM,
    changePasswordVM: ChangePasswordVM
) {
    val state by viewModel.uiState.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var showFixedDialog by remember { mutableStateOf(false) }
    var showChangePasswordModal by remember { mutableStateOf(false) }
    var fabExpanded by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mi Portafolio", style = MaterialTheme.typography.headlineSmall) },
                actions = {
                    IconButton(onClick = { showChangePasswordModal = true }) {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = "Cambiar contraseña",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = { viewModel.logout() }) {
                        Icon(
                            Icons.Default.ExitToApp,
                            contentDescription = "Logout",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                    IconButton(onClick = { viewModel.loadData() }) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                )
            )
        },
        floatingActionButton = {
            DashboardFabMenu(
                expanded = fabExpanded,
                onExpandClick = { fabExpanded = !fabExpanded },
                onAddStock = { 
                    fabExpanded = false
                    showDialog = true 
                },
                onAddFixed = {
                    fabExpanded = false
                    showFixedDialog = true
                }
            )
        }
    ) { paddingValues ->
        when {
            state.isLoading -> {
                Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            state.errorMessage != null -> {
                Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = state.errorMessage!!,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadData() }) {
                            Text("Reintentar")
                        }
                    }
                }
            }

            else -> {
                if (showChangePasswordModal) {
                    ChangePasswordModal(
                        viewModel = changePasswordVM,
                        onClose = { showChangePasswordModal = false }
                    )
                }
                if (showDialog) {
                    SelectStockModal(
                        onClose = { showDialog = false },
                        reloadData = { viewModel.loadData() },
                        viewModel = viewModelModal
                    )
                }
                if (showFixedDialog) {
                    AddFixedPortfolioModal(
                        onClose = { showFixedDialog = false },
                        reloadData = { viewModel.loadData() },
                        viewModel = addFixedPortfolioModalVM
                    )
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    item {
                        Column(modifier = Modifier.padding(16.dp)) {
                            TotalValueCard(state.totalValue, state.totalValueFixed)
                            if (state.chartData.data.isNotEmpty()) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 8.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                ) {
                                    PortfolioPieChart(
                                        uiState = state,
                                        modifier = Modifier.height(220.dp).padding(8.dp)
                                    )
                                }
                            }
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
                    }

                    when (state.selectedTabIndex) {
                        0 -> StockListTab(state, viewModel)
                        1 -> InfoTabDash(state)
                    }

                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }
}

