package com.frontend.finanzasdashfront.views.portfolio

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import com.frontend.finanzasdashfront.dto.dividend.DividendDto
import com.frontend.finanzasdashfront.dto.enums.DividendTypeEnum
import com.frontend.finanzasdashfront.utils.toLabel

@Composable
fun DividendCard(dividend: DividendDto) {
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