package com.frontend.finanzasdashfront.views.portfolio.modal

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.frontend.finanzasdashfront.enum.FixedPortfolioOperationTypeEnum

@Composable
fun FixedPortfolioOperationModal(
    onDismissRequest: () -> Unit,
    onConfirm: (Float, FixedPortfolioOperationTypeEnum) -> Unit,
    operationType: FixedPortfolioOperationTypeEnum
) {
    var amountText by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    val title = if (operationType == FixedPortfolioOperationTypeEnum.deposit) "Realizar Abono" else "Realizar Retiro"
    val description = if (operationType == FixedPortfolioOperationTypeEnum.deposit) {
        "Ingresa la cantidad que deseas abonar a este portafolio."
    } else {
        "Ingresa la cantidad que deseas retirar de este portafolio."
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = title, color = MaterialTheme.colorScheme.onSurface)
        },
        text = {
            Column {
                Text(text = description, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { 
                        amountText = it
                        errorMessage = null
                    },
                    label = { Text("Monto ($)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = errorMessage != null,
                    supportingText = if (errorMessage != null) {
                        { Text(errorMessage!!) }
                    } else null,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amount = amountText.toFloatOrNull()
                    if (amount == null || amount <= 0) {
                        errorMessage = "Por favor ingresa un monto válido mayor a 0."
                        return@Button
                    }
                    onConfirm(amount, operationType)
                }
            ) {
                Text(if (operationType == FixedPortfolioOperationTypeEnum.deposit) "Abonar" else "Retirar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancelar")
            }
        }
    )
}
