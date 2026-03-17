package com.frontend.finanzasdashfront.views.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.frontend.finanzasdashfront.viewmodel.dashboard.ChangePasswordVM

@Composable
fun ChangePasswordModal(
    viewModel: ChangePasswordVM,
    onClose: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Dialog(onDismissRequest = {
        viewModel.reset()
        onClose()
    }) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 3.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // ── Header ─────────────────────────────────────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            "Cambiar Contraseña",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            "Ingresa tu contraseña actual y la nueva",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = {
                        viewModel.reset()
                        onClose()
                    }) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar")
                    }
                }

                HorizontalDivider()

                // ── Loading / Success / Error ───────────────────────────
                if (state.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(120.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    if (state.successMessage.isNotBlank()) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = state.successMessage,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(12.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    if (state.errorMessage.isNotBlank()) {
                        Text(
                            text = state.errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }

                    // ── Contraseña actual ───────────────────────────────
                    PasswordField(
                        label = "Contraseña actual",
                        value = state.currentPassword,
                        isVisible = state.isCurrentPasswordVisible,
                        onValueChange = viewModel::onCurrentPasswordChanged,
                        onToggleVisibility = viewModel::toggleCurrentPasswordVisible
                    )

                    // ── Nueva contraseña ────────────────────────────────
                    PasswordField(
                        label = "Nueva contraseña",
                        value = state.newPassword,
                        isVisible = state.isNewPasswordVisible,
                        onValueChange = viewModel::onNewPasswordChanged,
                        onToggleVisibility = viewModel::toggleNewPasswordVisible
                    )

                    // ── Confirmar nueva contraseña ──────────────────────
                    PasswordField(
                        label = "Confirmar nueva contraseña",
                        value = state.confirmPassword,
                        isVisible = state.isConfirmPasswordVisible,
                        onValueChange = viewModel::onConfirmPasswordChanged,
                        onToggleVisibility = viewModel::toggleConfirmPasswordVisible
                    )

                    // ── Botón guardar ───────────────────────────────────
                    Button(
                        onClick = { viewModel.changePassword() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        enabled = state.currentPassword.isNotBlank() &&
                                state.newPassword.isNotBlank() &&
                                state.confirmPassword.isNotBlank()
                    ) {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Guardar contraseña", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }
    }
}

@Composable
private fun PasswordField(
    label: String,
    value: String,
    isVisible: Boolean,
    onValueChange: (String) -> Unit,
    onToggleVisibility: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, style = MaterialTheme.typography.bodySmall) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
        trailingIcon = {
            IconButton(onClick = onToggleVisibility) {
                Icon(
                    imageVector = if (isVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = if (isVisible) "Ocultar" else "Mostrar"
                )
            }
        }
    )
}
