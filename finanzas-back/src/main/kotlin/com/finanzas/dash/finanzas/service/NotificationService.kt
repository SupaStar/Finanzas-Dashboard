package com.finanzas.dash.finanzas.service

import com.finanzas.dash.finanzas.dto.NotificationDto
import com.finanzas.dash.finanzas.entity.Notification
import com.finanzas.dash.finanzas.entity.User
import com.finanzas.dash.finanzas.repository.NotificationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class NotificationService(
    private val notificationRepository: NotificationRepository
) {

    @Transactional(readOnly = true)
    fun getUnreadNotifications(username: String): List<NotificationDto> {
        return notificationRepository.findByUserUsernameAndIsReadFalseOrderByCreatedAtDesc(username)
            .map { it.toDto() }
    }

    @Transactional
    fun markAsRead(id: Int, username: String) {
        val notification = notificationRepository.findById(id).orElse(null)
        if (notification != null && notification.user?.username == username) {
            notification.isRead = true
            notificationRepository.save(notification)
        }
    }

    @Transactional
    fun markAllAsRead(username: String) {
        notificationRepository.markAllAsReadByUsername(username)
    }

    @Transactional
    fun createNotification(user: User, message: String, link: String?) {
        val notification = Notification().apply {
            this.user = user
            this.message = message
            this.link = link
            this.isRead = false
        }
        notificationRepository.save(notification)
    }

    private fun Notification.toDto(): NotificationDto {
        return NotificationDto(
            id = this.id,
            message = this.message,
            link = this.link,
            isRead = this.isRead,
            createdAt = this.createdAt
        )
    }
}
