package com.frontend.finanzasdashfront.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.material3.*

@Composable
fun DashboardScreen(onLogout: () -> Unit) {
    Column() {
        Text(text = "Dashboard")
        Button(onClick = {
            onLogout()
        }){
            Text("Logout")
        }
    }
}