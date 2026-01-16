package com.frontend.finanzasdashfront.views.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.frontend.finanzasdashfront.dto.portfolio.PortfolioDto
import com.frontend.finanzasdashfront.ui.theme.Surface
import com.frontend.finanzasdashfront.utils.formatCurrency

@Composable
fun PortfolioRow(item: PortfolioDto, onClick: (Long) -> Unit) {
    OutlinedCard(
        onClick = { onClick(item.portfolioId) },
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(item.Stock.symbol, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = CircleShape
                ) {
                    Text(
                        "${item.totalQuantity} títulos",
                        Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                InfoColumn("Invertido", "$${(item.avgPrice.toDouble() * item.totalQuantity.toDouble()).toFloat().formatCurrency()}", Modifier.weight(1f))
                InfoColumn("Valor Actual", "$${(item.totalQuantity.toDouble() * item.Stock.closeDay.toDouble()).toFloat().formatCurrency()}", Modifier.weight(1f), isHighlight = true)
            }
        }
    }
}

@Composable
fun InfoColumn(label: String, value: String, modifier: Modifier, isHighlight: Boolean = false) {
    Column(modifier) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
        Text(
            value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Normal,
            color = if (isHighlight) MaterialTheme.colorScheme.primary else Color.Unspecified
        )
    }
}