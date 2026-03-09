package com.frontend.finanzasdashfront.views.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.frontend.finanzasdashfront.utils.formatCurrency

@Composable
fun TotalValueCard(totalValue: Double) {
    Card(
        modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(
                "Valor Total del Portafolio",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
            )
            Text(
                text = "$${totalValue.toFloat().formatCurrency()}",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}