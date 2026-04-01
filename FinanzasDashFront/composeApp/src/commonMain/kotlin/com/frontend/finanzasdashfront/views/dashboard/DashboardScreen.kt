package com.frontend.finanzasdashfront.views.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
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
import com.frontend.finanzasdashfront.AppModule
import com.frontend.finanzasdashfront.routes.routers.DashboardScreens
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import kotlinx.coroutines.launch

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
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val isDark by AppModule.themeManager.isDark.collectAsState()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(16.dp))
                Text("Menú de Opciones", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge)
                Divider()
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                    label = { Text("Mi Portafolio (Dashboard)") },
                    selected = true,
                    onClick = { scope.launch { drawerState.close() } }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.List, contentDescription = null) },
                    label = { Text("Estados de Cuenta") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        AppModule.dashboardRouter.goTo(DashboardScreens.Statements)
                    }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Refresh, contentDescription = null) },
                    label = { Text(if (state.showUsdAsMxn) "Modo USD: Mostrar original" else "Modo USD: Convertir a MXN") },
                    selected = false,
                    onClick = { viewModel.toggleUsdDisplay() }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Refresh, contentDescription = null) },
                    label = { Text(if (isDark) "Tema Claro" else "Tema Oscuro") },
                    selected = false,
                    onClick = { AppModule.themeManager.toggleTheme() }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    label = { Text("Cambiar Contraseña") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        showChangePasswordModal = true
                    }
                )
                Spacer(Modifier.weight(1f))
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.ExitToApp, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
                    label = { Text("Cerrar Sesión", color = MaterialTheme.colorScheme.error) },
                    selected = false,
                    onClick = { viewModel.logout() }
                )
                Spacer(Modifier.height(16.dp))
            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Mi Portafolio", style = MaterialTheme.typography.headlineSmall) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menú")
                        }
                    },
                    actions = {
                        IconButton(onClick = { viewModel.loadData() }) {
                            Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = MaterialTheme.colorScheme.primary)
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
                            TabRow(
                                selectedTabIndex = state.selectedTabIndex,
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha=0.5f),
                                contentColor = MaterialTheme.colorScheme.primary
                            ) {
                                state.optionsTabs.forEachIndexed { index, title ->
                                    Tab(
                                        selected = state.selectedTabIndex == index,
                                        onClick = { viewModel.onTabIndexChanged(index) },
                                        text = {
                                            Text(
                                                title,
                                                style = MaterialTheme.typography.titleSmall,
                                                color = if (state.selectedTabIndex == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                            )
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

                    }

                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}

