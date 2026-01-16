package com.frontend.finanzasdashfront.views.portfolio

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.frontend.finanzasdashfront.model.portfolio.PortfolioDetailUiState
import com.frontend.finanzasdashfront.ui.component.GenericExposedDropdown
import com.frontend.finanzasdashfront.utils.formatCurrency
import com.frontend.finanzasdashfront.utils.year

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoTab(
    uiState: PortfolioDetailUiState,
    onYearSelected: (year: String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Resumen Financiero",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        GenericExposedDropdown(
            label = "Año a filtrar",
            options = uiState.yearsDividends,
            selectedOption = uiState.yearDividendsSelected,
            onOptionSelected = { year -> onYearSelected(year) },
            leadingIcon = Icons.Default.DateRange
        )

        if (uiState.yearDividendsSelected.isNotEmpty()) {
            val yearInt = uiState.yearDividendsSelected.toIntOrNull() ?: 0
            val operations = uiState.operations.filter { it.year() == yearInt }
            val dividends = uiState.dividends.filter { it.year() == yearInt }

            val totalFees = operations.sumOf { it.fee.toDouble() }.toFloat()
            val totalTaxes = (operations.sumOf { it.tax.toDouble() } + dividends.sumOf { it.tax.toDouble() }).toFloat()

            val totalDividends = dividends.sumOf { it.netValue.toDouble() }.toFloat()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                InfoCard(
                    title = "Comisiones",
                    amount = totalFees.formatCurrency(),
                    icon = Icons.Default.ReceiptLong,
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier.weight(1f)
                )
                InfoCard(
                    title = "Impuestos",
                    amount = totalTaxes.formatCurrency(),
                    icon = Icons.Default.AccountBalanceWallet,
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    modifier = Modifier.weight(1f)
                )
                InfoCard(
                    title = "N Rentas",
                    amount = totalDividends.formatCurrency(),
                    icon = Icons.Default.AccountBalanceWallet,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.weight(1f)
                )
            }
        } else {
            // Estado vacío o instrucción
            Box(Modifier.fillMaxWidth().padding(top = 32.dp), contentAlignment = Alignment.Center) {
                Text("Selecciona un año para ver el desglose", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun InfoCard(
    title: String,
    amount: String,
    icon: ImageVector,
    containerColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSecondaryContainer)
            Text(title, style = MaterialTheme.typography.labelMedium)
            Text(
                amount,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}