package com.frontend.finanzasdashfront.views.portfolio

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.frontend.finanzasdashfront.viewmodel.portfolio.PortfolioViewModel
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.draw.rotate
import com.frontend.finanzasdashfront.utils.formatCurrency
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
            TopAppBar(
                title = { Text("Acción: ${state.stockName}") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AnimatedVisibility(
                    visible = expanded,
                    enter = fadeIn() + expandVertically(expandFrom = Alignment.Bottom),
                    exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Bottom)
                ) {
                    Column(horizontalAlignment = Alignment.End) {
                        SmallFloatingActionButton(
                            onClick = { showDividendModal = !showDividendModal },
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        ) {
                            Text("Dividendo", modifier = Modifier.padding(horizontal = 12.dp))
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        SmallFloatingActionButton(
                            onClick = { showOperationModal = !showOperationModal },
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                        ) {
                            Text("Operación", modifier = Modifier.padding(horizontal = 12.dp))
                        }
                    }
                }

                val rotation by animateFloatAsState(
                    targetValue = if (expanded) 45f else 0f,
                    label = "Rotation"
                )

                FloatingActionButton(
                    onClick = { expanded = !expanded },
                    containerColor = if (expanded) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Menu",
                        modifier = Modifier.rotate(rotation)
                    )
                }
            }
        }
    ) { paddingValues ->
        if (state.isLoading) {
            Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (state.errorMessage != null) {
            Text(text = state.errorMessage!!, modifier = Modifier.padding(paddingValues))
        } else {
            if (showOperationModal) {
                AddOperationModal(
                    viewModel = addOperationVM,
                    onClose = { showOperationModal = false },
                    reloadOperations = { viewModel.loadPortfolioData() },
                    idPorfolio = state.portfolioid
                )
            }
            if(showDividendModal){
                AddDividendModal(
                    viewModel = addDividendVm,
                    onClose = { showDividendModal = false },
                    reloadDividends = { viewModel.loadPortfolioData() },
                    idPorfolio = state.portfolioid
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    item { SectionHeader("Operaciones", MaterialTheme.colorScheme.primary) }
                    items(state.operations) { operation ->
                        OperationCard(operation)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
                Text("Total de dividendos $${state.dividends.sumOf { it.netValue.toDouble() }.toFloat().formatCurrency()}")
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    item { SectionHeader("Dividendos", MaterialTheme.colorScheme.tertiary) }
                    items(state.dividends) { dividend ->
                        DividendCard(dividend)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, color: Color) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(title, style = MaterialTheme.typography.titleMedium, color = color)
        HorizontalDivider(thickness = 2.dp, color = color.copy(alpha = 0.5f))
    }
}