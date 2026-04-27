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
    fun getUnreadNotifications(userIdString: String): List<NotificationDto> {
        val userId = userIdString.toLongOrNull() ?: return emptyList()
        return notificationRepository.findByUserUserIdAndIsReadFalseOrderByCreatedAtDesc(userId)
            .map { it.toDto() }
    }

    @Transactional
    fun markAsRead(id: Int, userIdString: String) {
        val userId = userIdString.toLongOrNull() ?: return
        val notification = notificationRepository.findById(id).orElse(null)
        if (notification != null && notification.user?.userId == userId) {
            notification.isRead = true
            notificationRepository.save(notification)
        }
    }

    @Transactional
    fun markAllAsRead(userIdString: String) {
        val userId = userIdString.toLongOrNull() ?: return
        notificationRepository.markAllAsReadByUserId(userId)
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
