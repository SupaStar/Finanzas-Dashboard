package com.frontend.finanzasdashfront.views.dividend

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.frontend.finanzasdashfront.AppModule
import com.frontend.finanzasdashfront.dto.dividend.DividendCalendarEventDto
import com.frontend.finanzasdashfront.routes.routers.DashboardScreens
import com.frontend.finanzasdashfront.utils.formatCurrency
import com.frontend.finanzasdashfront.viewmodel.dividend.DividendCalendarViewModel
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DividendCalendarScreen(viewModel: DividendCalendarViewModel) {
    val state by viewModel.uiState.collectAsState()
    var selectedDate by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Calendario de Dividendos") },
                navigationIcon = {
                    IconButton(onClick = { AppModule.dashboardRouter.goTo(DashboardScreens.Dashboard) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            MonthSelector(
                month = state.selectedMonth,
                year = state.selectedYear,
                onPrevious = { viewModel.previousMonth() },
                onNext = { viewModel.nextMonth() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (state.errorMessage != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = state.errorMessage!!, color = MaterialTheme.colorScheme.error)
                }
            } else if (state.data != null) {
                SummaryCards(
                    paid = state.data!!.summary.paidMonth,
                    expected = state.data!!.summary.expectedMonth,
                    pending = state.data!!.summary.pendingMonth
                )

                Spacer(modifier = Modifier.height(16.dp))

                CalendarGrid(
                    month = state.selectedMonth,
                    year = state.selectedYear,
                    events = state.data!!.events,
                    onDateSelected = { selectedDate = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (selectedDate != null) {
                    val eventsForDate = state.data!!.events.filter { it.date == selectedDate }
                    Text(
                        text = "Eventos del $selectedDate",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    if (eventsForDate.isEmpty()) {
                        Text("No hay eventos en esta fecha.")
                    } else {
                        LazyColumn {
                            items(eventsForDate) { event ->
                                EventCard(event)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MonthSelector(month: Int, year: Int, onPrevious: () -> Unit, onNext: () -> Unit) {
    val monthNames = listOf(
        "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
        "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPrevious) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Anterior")
        }
        Text(
            text = "${monthNames[month - 1]} $year",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        IconButton(onClick = onNext) {
            Icon(Icons.Default.ArrowForward, contentDescription = "Siguiente")
        }
    }
}

@Composable
fun SummaryCards(paid: Double, expected: Double, pending: Double) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        SummaryCard(
            title = "Pagado",
            amount = paid,
            color = Color(0xFF4CAF50),
            modifier = Modifier.weight(1f)
        )
        SummaryCard(
            title = "Esperado",
            amount = expected,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(1f)
        )
        SummaryCard(
            title = "Falta",
            amount = pending,
            color = Color(0xFFFF9800),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun SummaryCard(title: String, amount: Double, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(color))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, style = MaterialTheme.typography.bodySmall, color = color)
            Text(
                text = amount.toFloat().formatCurrency(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun CalendarGrid(month: Int, year: Int, events: List<DividendCalendarEventDto>, onDateSelected: (String) -> Unit) {
    // Basic calendar logic
    val daysInMonth = when (month) {
        2 -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28
        4, 6, 9, 11 -> 30
        else -> 31
    }

    val firstDayOfMonthStr = "$year-${month.toString().padStart(2, '0')}-01"
    
    // Day of week calculation (1 = Monday, 7 = Sunday)
    // A simple way to get day of week for the 1st of the month:
    // This is a simplified Zeller's congruence
    val m = if (month < 3) month + 12 else month
    val Y = if (month < 3) year - 1 else year
    val k = Y % 100
    val j = Y / 100
    val h = (1 + 13 * (m + 1) / 5 + k + k / 4 + j / 4 + 5 * j) % 7
    // h = 0 is Saturday, 1 is Sunday, 2 is Monday...
    val dayOfWeek = ((h + 5) % 7) + 1 // 1 = Monday, ..., 7 = Sunday

    val emptyCells = dayOfWeek - 1

    val daysList = (1..daysInMonth).toList()

    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            listOf("L", "M", "Mi", "J", "V", "S", "D").forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.height(260.dp),
            userScrollEnabled = false
        ) {
            items(emptyCells) {
                Box(modifier = Modifier.height(40.dp))
            }
            items(daysList) { day ->
                val dateStr = "$year-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}"
                val dayEvents = events.filter { it.date == dateStr }
                val hasPaid = dayEvents.any { it.isPaid }
                val hasPending = dayEvents.any { !it.isPaid }

                val backgroundColor = when {
                    hasPaid -> Color(0xFF4CAF50).copy(alpha = 0.2f)
                    hasPending -> Color(0xFF2196F3).copy(alpha = 0.2f)
                    else -> Color.Transparent
                }

                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(backgroundColor)
                        .clickable { onDateSelected(dateStr) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = day.toString(),
                        color = if (hasPaid || hasPending) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        fontWeight = if (hasPaid || hasPending) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}

@Composable
fun EventCard(event: DividendCalendarEventDto) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (event.isPaid) Color(0xFF4CAF50).copy(alpha = 0.1f) else Color(0xFF2196F3).copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = event.ticker,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = event.company ?: "Anuncio",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = if (event.isPaid) "Pagado" else "Anunciado (${event.type})",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (event.isPaid) Color(0xFF4CAF50) else Color(0xFF2196F3)
                )
            }
            Text(
                text = event.amount.toFloat().formatCurrency(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
