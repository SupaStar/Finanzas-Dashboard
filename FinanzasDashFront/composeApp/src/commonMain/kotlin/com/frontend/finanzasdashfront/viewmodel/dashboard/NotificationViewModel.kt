package com.frontend.finanzasdashfront.viewmodel.dashboard

import com.frontend.finanzasdashfront.api.services.NotificationService
import com.frontend.finanzasdashfront.dto.NotificationDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

data class NotificationUIState(
    val unreadNotifications: List<NotificationDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class NotificationViewModel(
    private val notificationService: NotificationService
) {
    private val _uiState = MutableStateFlow(NotificationUIState())
    val uiState: StateFlow<NotificationUIState> = _uiState.asStateFlow()

    private val scope = CoroutineScope(Dispatchers.Main + Job())
    private var refreshJob: Job? = null

    init {
        startAutoRefresh()
    }

    fun startAutoRefresh() {
        refreshJob?.cancel()
        refreshJob = scope.launch {
            while (isActive) {
                loadUnreadNotifications()
                delay(2 * 60 * 1000L) // 2 minutes
            }
        }
    }

    fun stopAutoRefresh() {
        refreshJob?.cancel()
    }

    fun loadUnreadNotifications() {
        scope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val notifications = notificationService.getUnreadNotifications()
                _uiState.update { it.copy(unreadNotifications = notifications, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun markAsRead(id: Int) {
        scope.launch {
            try {
                notificationService.markAsRead(id)
                // Remove from the local list to update UI immediately
                _uiState.update { state ->
                    state.copy(unreadNotifications = state.unreadNotifications.filter { it.id != id })
                }
            } catch (e: Exception) {
                // If it fails, reload the state from backend
                loadUnreadNotifications()
            }
        }
    }

    fun markAllAsRead() {
        scope.launch {
            try {
                notificationService.markAllAsRead()
                _uiState.update { it.copy(unreadNotifications = emptyList()) }
            } catch (e: Exception) {
                loadUnreadNotifications()
            }
        }
    }
}
