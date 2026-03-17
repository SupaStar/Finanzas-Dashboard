package com.frontend.finanzasdashfront.views.portfolio

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import com.frontend.finanzasdashfront.dto.dividend.DividendDto
import com.frontend.finanzasdashfront.dto.enums.DividendTypeEnum
import com.frontend.finanzasdashfront.ui.component.SwipeRevealItem
import com.frontend.finanzasdashfront.utils.toLabel

@Composable
fun DividendCard(
    dividend: DividendDto,
    onEdit: (DividendDto) -> Unit = {},
    onDelete: (DividendDto) -> Unit = {}
) {
    SwipeRevealItem(
        modifier = Modifier.padding(bottom = 8.dp),
        actions = {
            IconButton(
                onClick = {
                    println("Edit dividend: $dividend")
                    onEdit(dividend)
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
                    println("Delete dividend: $dividend")
                    onDelete(dividend)
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
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Fecha Pago: ${dividend.paidDate}", style = MaterialTheme.typography.labelMedium)
                Text("${dividend.dividendType.toLabel()}: ${dividend.value} ${dividend.currencyCode}", fontWeight = Bold)

                if (dividend.currencyCode != "MXN") {
                    Text("Tipo de cambio: ${dividend.exchangeRate}", style = MaterialTheme.typography.bodySmall)
                }

                Text("Neto: ${dividend.netValue} ${dividend.currencyCode}", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}