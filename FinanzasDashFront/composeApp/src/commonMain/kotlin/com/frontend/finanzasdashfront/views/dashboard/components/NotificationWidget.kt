package com.frontend.finanzasdashfront.views.dashboard.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.frontend.finanzasdashfront.dto.NotificationDto
import com.frontend.finanzasdashfront.viewmodel.dashboard.NotificationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationBell(
    viewModel: NotificationViewModel,
    onClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val unreadCount = state.unreadNotifications.size

    IconButton(onClick = onClick) {
        if (unreadCount > 0) {
            BadgedBox(
                badge = {
                    Badge(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    ) {
                        Text(if (unreadCount > 99) "99+" else unreadCount.toString())
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notificaciones"
                )
            }
        } else {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notificaciones"
            )
        }
    }
}

@Composable
fun NotificationDrawerContent(
    viewModel: NotificationViewModel,
    onCloseDrawer: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    var selectedNotification by remember { mutableStateOf<NotificationDto?>(null) }

    ModalDrawerSheet(
        modifier = Modifier.width(320.dp),
        drawerContainerColor = MaterialTheme.colorScheme.surface,
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Notificaciones",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
                if (state.unreadNotifications.isNotEmpty()) {
                    TextButton(onClick = { viewModel.markAllAsRead() }) {
                        Text("Leer todas", color = MaterialTheme.colorScheme.primary)
                    }
                }
            }

            Divider()

            if (state.isLoading && state.unreadNotifications.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (state.unreadNotifications.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "No tienes notificaciones.",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(state.unreadNotifications) { notification ->
                        NotificationItem(
                            notification = notification,
                            onClick = {
                                selectedNotification = notification
                            }
                        )
                        HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
                    }
                }
            }
        }
    }

    selectedNotification?.let { notification ->
        NotificationModal(
            notification = notification,
            onDismiss = {
                viewModel.markAsRead(notification.id)
                selectedNotification = null
                if (state.unreadNotifications.size == 1) { // It was the last one
                    onCloseDrawer()
                }
            }
        )
    }
}

@Composable
fun NotificationItem(notification: NotificationDto, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        color = Color.Transparent
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = notification.message,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = notification.createdAt, // Or format it properly if needed
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
fun NotificationModal(notification: NotificationDto, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Notificación", style = MaterialTheme.typography.titleLarge)
        },
        text = {
            Column {
                Text(text = notification.message, style = MaterialTheme.typography.bodyLarge)
                if (!notification.link.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Enlace: ${notification.link}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Cerrar y marcar como leída")
            }
        }
    )
}
