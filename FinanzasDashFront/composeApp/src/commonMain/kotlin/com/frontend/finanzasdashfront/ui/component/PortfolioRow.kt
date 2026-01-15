package com.frontend.finanzasdashfront.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.frontend.finanzasdashfront.dto.portfolio.PortfolioDto
import kotlin.math.roundToInt

@Composable
fun PortfolioRow(item: PortfolioDto, onItemClicked: (Long) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable{onItemClicked(item.portfolioId)}
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1.5f)) {
            Text(item.Stock.symbol, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
            Text(item.Stock.name, style = MaterialTheme.typography.bodySmall, color = Color.Gray, maxLines = 1)
        }
        Text(
            text = "${item.totalQuantity}",
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "$${item.avgPrice}",
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "$${item.avgPrice.toDouble() * item.totalQuantity.toDouble()}",
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "$${item.totalQuantity.toDouble() * item.Stock.closeDay.toDouble()}",
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}