package com.frontend.finanzasdashfront.views.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.frontend.finanzasdashfront.model.dashboard.DashboardUiState
import com.frontend.finanzasdashfront.viewmodel.dashboard.DashboardViewModel
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi

@OptIn(ExperimentalMaterial3Api::class, ExperimentalKoalaPlotApi::class)
@Composable
fun StockListTab(state: DashboardUiState, viewModel: DashboardViewModel) {
    Text(
        text = "Detalle de Activos",
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
    )

    OutlinedTextField(
        value = state.filterStock,
        onValueChange = { viewModel.onFilterChanged(newValue = it) },
        label = { Text("Buscar") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
    )

    if (state.filterStock == "") {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                ,
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(state.items) { portfolioItem ->
                PortfolioRow(
                    item = portfolioItem,
                    onClick = { viewModel.goToDetail(portfolioItem.portfolioId) },
                    usdValue = state.usdValue
                )
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                ,
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(state.filteredStocks) { portfolioItem ->
                PortfolioRow(
                    item = portfolioItem,
                    onClick = { viewModel.goToDetail(portfolioItem.portfolioId) },
                    usdValue = state.usdValue
                )
            }
        }
    }

}