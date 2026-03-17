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
fun PortfolioRow(item: PortfolioDto, onClick: (Long) -> Unit, usdValue: Float, showUsdAsMxn: Boolean) {
    // Si la acción es extranjera o se compro originalmente en dólares:
    // avgPrice refleja el costo histórico en MXN en el backend (ver PortfolioService), o en la misma moneda. 
    // Para no errar con el promedio y el USD actual, siempre calcularemos basado en la preferencia de vista.

    val valorCompraFinal: Float
    val valorActualFinal: Float

    if (item.Stock.currency == "MXN") {
        valorCompraFinal = item.avgPrice * item.totalQuantity
        valorActualFinal = item.Stock.closeDay * item.totalQuantity
    } else {
        // Es USD. Si NO showUsdAsMxn, se asume ver los valores crudos en USD.
        // Ojo: Si item.avgPrice viene en MXN desde backend (como usualmente sucede por tipo de cambio de la operacion),
        // habría que dividir por usdValue para mostrarlo en dólares puros.
        // Asumiendo que item.avgPrice e item.Stock.closeDay vienen en la moneda nativa de la Acción (USD en este caso):
        
        if (showUsdAsMxn) {
            valorCompraFinal = (item.avgPrice * item.totalQuantity) * usdValue
            valorActualFinal = (item.Stock.closeDay * item.totalQuantity) * usdValue
        } else {
            valorCompraFinal = item.avgPrice * item.totalQuantity
            valorActualFinal = item.Stock.closeDay * item.totalQuantity
        }
    }

    val plusMinus = if (valorCompraFinal > 0f) {
        (valorActualFinal - valorCompraFinal) / valorCompraFinal * 100
    } else {
        Float.NaN
    }
    
    // Obtener símbolo a mostrar
    val monedaSimbolo = if (item.Stock.currency == "MXN" || showUsdAsMxn) "MXN" else item.Stock.currency

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
                Text(
                    item.Stock.symbol,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                val pActualLabel = if (item.Stock.currency == "MXN" || !showUsdAsMxn) {
                    item.Stock.closeDay.formatCurrency()
                } else {
                    (item.Stock.closeDay * usdValue).formatCurrency()
                }

                Text(
                    "Precio actual ($monedaSimbolo): $pActualLabel",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

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

            Row(Modifier.fillMaxWidth()) {
                if (item.Stock.currency == "MXN") {
                    InfoColumn(
                        "Invertido ($monedaSimbolo)",
                        "$${
                            valorCompraFinal
                                .formatCurrency()
                        }",
                        Modifier.weight(1f)
                    )
                    InfoColumn(
                        "Valor Actual ($monedaSimbolo)",
                        "$${
                            valorActualFinal
                                .formatCurrency()
                        }",
                        Modifier.weight(1f),
                        isHighlight = true
                    )
                    if (!plusMinus.isNaN()) {
                        InfoColumn(
                            "Plus Minus",
                            "${
                                plusMinus.formatCurrency()
                            } %",
                            Modifier.weight(1f),
                            isHighlight = true
                        )
                    }
                } else {
                    InfoColumn(
                        "Valor Actual ($monedaSimbolo)",
                        "$${
                            valorActualFinal
                                .formatCurrency()
                        }",
                        Modifier.weight(1f),
                        isHighlight = true
                    )
                }
            }
        }
    }
}

@Composable
fun InfoColumn(label: String, value: String, modifier: Modifier, isHighlight: Boolean = false) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center
        )
        Text(
            value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Normal,
            color = if (isHighlight) MaterialTheme.colorScheme.primary else Color.Unspecified,
            textAlign = TextAlign.Center
        )
    }
}