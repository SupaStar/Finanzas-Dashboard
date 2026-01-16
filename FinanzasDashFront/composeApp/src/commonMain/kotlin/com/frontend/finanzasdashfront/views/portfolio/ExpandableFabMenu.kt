package com.frontend.finanzasdashfront.views.portfolio

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.*
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp

@Composable
fun ExpandableFabMenu(
    expanded: Boolean,
    onExpandClick: () -> Unit,
    onAddOperation: () -> Unit,
    onAddDividend: () -> Unit
) {
    val rotation by animateFloatAsState(if (expanded) 45f else 0f, label = "rot")

    Column(horizontalAlignment = Alignment.End) {
        AnimatedVisibility(visible = expanded, enter = fadeIn() + slideInVertically { it / 2 }, exit = fadeOut() + slideOutVertically { it / 2 }) {
            Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ExtendedFloatingActionButton(
                    text = { Text("Dividendo") },
                    icon = { Icon(Icons.Default.Add, null) },
                    onClick = onAddDividend,
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
                ExtendedFloatingActionButton(
                    text = { Text("Operación") },
                    icon = { Icon(Icons.Default.Add, null) },
                    onClick = onAddOperation,
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        FloatingActionButton(onClick = onExpandClick, shape = CircleShape) {
            Icon(Icons.Default.Add, null, modifier = Modifier.rotate(rotation))
        }
    }
}