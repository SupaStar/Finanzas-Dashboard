package com.frontend.finanzasdashfront.views.portfolio.modal

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun EditFixedPortfolioAmountModal(
    initialAmount: Float,
    onDismissRequest: () -> Unit,
    onConfirm: (Float) -> Unit
) {
    var amountText by remember { mutableStateOf(initialAmount.toString()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = "Editar Monto Base")
        },
        text = {
            Column {
                Text(text = "Modifica el monto base de este portafolio. Ten en cuenta que esto cambiará el saldo total directamente.")
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { 
                        amountText = it
                        errorMessage = null
                    },
                    label = { Text("Nuevo Monto ($)") },
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
                    onConfirm(amount)
                }
            ) {
                Text("Actualizar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancelar")
            }
        }
    )
}
