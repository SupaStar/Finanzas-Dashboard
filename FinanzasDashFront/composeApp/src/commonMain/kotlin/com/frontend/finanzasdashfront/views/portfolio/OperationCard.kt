package com.frontend.finanzasdashfront.views.portfolio

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import com.frontend.finanzasdashfront.dto.enums.OperationTypeEnum
import com.frontend.finanzasdashfront.dto.operation.OperationDto
import com.frontend.finanzasdashfront.ui.component.SwipeRevealItem
import com.frontend.finanzasdashfront.utils.formatCurrency
import com.frontend.finanzasdashfront.utils.toLabel

@Composable
fun OperationCard(
    operation: OperationDto,
    onEdit: (OperationDto) -> Unit = {},
    onDelete: (OperationDto) -> Unit = {}
) {
    SwipeRevealItem(
        modifier = Modifier.padding(bottom = 8.dp),
        actions = {
            IconButton(
                onClick = {
                    println("Edit operation: $operation")
                    onEdit(operation)
                },
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            IconButton(
                onClick = {
                    println("Delete operation: $operation")
                    onDelete(operation)
                },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.errorContainer)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    ) {
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
                Text("Total: $${total.formatCurrency()}", style = MaterialTheme.typography.bodyLarge, fontWeight = Bold)
            }
        }
    }
}