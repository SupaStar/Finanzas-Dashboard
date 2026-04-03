package com.frontend.finanzasdashfront.views.dashboard.statements

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalUriHandler
import com.frontend.finanzasdashfront.AppModule
import com.frontend.finanzasdashfront.api.Constants
import com.frontend.finanzasdashfront.dto.response.statement.StatementResponseDto
import com.frontend.finanzasdashfront.viewmodel.dashboard.statements.StatementsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatementsScreen(viewModel: StatementsViewModel) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Estados de Cuenta") },
                navigationIcon = {
                    IconButton(onClick = { AppModule.dashboardRouter.goTo(com.frontend.finanzasdashfront.routes.routers.DashboardScreens.Dashboard) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            if (state.isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
        } else if (state.errorMessage != null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                    Spacer(Modifier.height(8.dp))
                    Text(state.errorMessage ?: "Error", color = MaterialTheme.colorScheme.error)
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = { viewModel.loadStatements() }) {
                        Text("Reintentar")
                    }
                }
            }
        } else {
            if (state.statements.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No tienes estados de cuenta generados aún.", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(state.statements) { statement ->
                        StatementCard(statement)
                    }
                }
            }
        }
        }
    }
}

@Composable
fun StatementCard(statement: StatementResponseDto) {
    val tokenManager = AppModule.tokenManager
    val mesLetra = when(statement.month) {
        1 -> "Enero"; 2 -> "Febrero"; 3 -> "Marzo"; 4 -> "Abril"; 5 -> "Mayo"; 6 -> "Junio"
        7 -> "Julio"; 8 -> "Agosto"; 9 -> "Septiembre"; 10 -> "Octubre"; 11 -> "Noviembre"; 12 -> "Diciembre"
        else -> statement.month.toString()
    }
    
    val uriHandler = LocalUriHandler.current
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Estado de Cuenta", style = MaterialTheme.typography.titleMedium)
                Text("$mesLetra, ${statement.year}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Button(onClick = {
                val token = tokenManager.getToken()
                val url = "${Constants.BaseUrl}${statement.downloadUrl}&token=$token"
                uriHandler.openUri(url)
            }) {
                Text("Descargar PDF")
            }
        }
    }
}
