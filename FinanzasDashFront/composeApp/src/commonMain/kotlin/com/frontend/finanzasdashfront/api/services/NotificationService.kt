package com.frontend.finanzasdashfront.api.services

import com.frontend.finanzasdashfront.api.Constants
import com.frontend.finanzasdashfront.dto.NotificationDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post

class NotificationService(private val client: HttpClient) {
    suspend fun getUnreadNotifications(): List<NotificationDto> {
        val response = client.get("${Constants.BaseUrl}/api/notifications/unread")
        return response.body()
    }

    suspend fun markAsRead(id: Int) {
        client.post("${Constants.BaseUrl}/api/notifications/$id/read")
    }

    suspend fun markAllAsRead() {
        client.post("${Constants.BaseUrl}/api/notifications/read-all")
    }
}
