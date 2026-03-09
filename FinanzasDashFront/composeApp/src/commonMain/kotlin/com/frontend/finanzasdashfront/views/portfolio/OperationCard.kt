package com.frontend.finanzasdashfront.views.portfolio

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import com.frontend.finanzasdashfront.dto.enums.OperationTypeEnum
import com.frontend.finanzasdashfront.dto.operation.OperationDto
import com.frontend.finanzasdashfront.utils.formatCurrency
import com.frontend.finanzasdashfront.utils.toLabel

@Composable
fun OperationCard(operation: OperationDto) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Fecha operacion: ${operation.operationDate}", style = MaterialTheme.typography.labelMedium)
            Text("Tipo: ${operation.operationType.toLabel()}", fontWeight = Bold)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Cantidad: ${operation.quantity}")
                Text("Precio: $${operation.price.formatCurrency()}")
            }
            Divider(Modifier.padding(vertical = 8.dp))
            val total = if (operation.quantity < 1f) operation.price else operation.total
            Text("Total: $${total}", style = MaterialTheme.typography.bodyLarge, fontWeight = Bold)
        }
    }
}