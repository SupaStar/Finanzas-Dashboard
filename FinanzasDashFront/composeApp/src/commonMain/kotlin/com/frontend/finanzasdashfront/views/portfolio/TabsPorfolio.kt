package com.frontend.finanzasdashfront.views.portfolio

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.frontend.finanzasdashfront.dto.dividend.DividendDto
import com.frontend.finanzasdashfront.dto.operation.OperationDto
import com.frontend.finanzasdashfront.utils.formatCurrency

@Composable
fun OperationTab(
    operations: List<OperationDto>,
    onEdit: (OperationDto) -> Unit = {},
    onDelete: (OperationDto) -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (operations.isEmpty()) {
            item { Text("No hay operaciones", Modifier.padding(16.dp), color = MaterialTheme.colorScheme.onSurface) }
        }
        items(operations, key = { it.operationId ?: it.hashCode() }) { operation ->
            OperationCard(
                operation = operation,
                onEdit = onEdit,
                onDelete = onDelete
            )
        }
    }
}

@Composable
fun DividendTab(
    dividends: List<DividendDto>,
    onEdit: (DividendDto) -> Unit = {},
    onDelete: (DividendDto) -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            val total = dividends.sumOf { it.netValue.toDouble() }.toFloat().formatCurrency()
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            ) {
                Text(
                    "Total Percibido: $$total",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
        if (dividends.isEmpty()) {
            item { Text("No hay dividendos", Modifier.padding(16.dp), color = MaterialTheme.colorScheme.onSurface) }
        }
        items(dividends, key = { it.dividendId ?: it.hashCode() }) { dividend ->
            DividendCard(
                dividend = dividend,
                onEdit = onEdit,
                onDelete = onDelete
            )
        }
    }
}
