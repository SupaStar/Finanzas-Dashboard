package com.frontend.finanzasdashfront.views.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.frontend.finanzasdashfront.model.dashboard.DashboardUiState
import com.frontend.finanzasdashfront.viewmodel.dashboard.DashboardViewModel
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi

import androidx.compose.foundation.lazy.LazyListScope

@OptIn(ExperimentalMaterial3Api::class, ExperimentalKoalaPlotApi::class)
fun LazyListScope.StockListTab(state: DashboardUiState, viewModel: DashboardViewModel) {
    item {
        Text(
            text = "Detalle de Activos",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedTextField(
                value = state.filterStock,
                onValueChange = { viewModel.onFilterChanged(newValue = it) },
                label = { Text("Buscar") },
                singleLine = true,
                modifier = Modifier.weight(1f),
                shape = MaterialTheme.shapes.medium,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("USD en MXN", style = MaterialTheme.typography.labelSmall)
                Switch(
                    checked = state.showUsdAsMxn,
                    onCheckedChange = { viewModel.toggleUsdDisplay() }
                )
            }
        }
    }

    if (state.filterStock == "") {
        items(state.items) { portfolioItem ->
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp)) {
                PortfolioRow(
                    item = portfolioItem,
                    onClick = { viewModel.goToDetail(portfolioItem.portfolioId) },
                    usdValue = state.usdValue,
                    showUsdAsMxn = state.showUsdAsMxn
                )
            }
        }
    } else {
        items(state.filteredStocks) { portfolioItem ->
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp)) {
                PortfolioRow(
                    item = portfolioItem,
                    onClick = { viewModel.goToDetail(portfolioItem.portfolioId) },
                    usdValue = state.usdValue,
                    showUsdAsMxn = state.showUsdAsMxn
                )
            }
        }
    }
}