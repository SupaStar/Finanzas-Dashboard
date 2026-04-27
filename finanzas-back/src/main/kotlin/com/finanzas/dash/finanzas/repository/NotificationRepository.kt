package com.finanzas.dash.finanzas.repository

import com.finanzas.dash.finanzas.entity.Notification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface NotificationRepository : JpaRepository<Notification, Int> {
    fun findByUserUserIdAndIsReadFalseOrderByCreatedAtDesc(userId: Long): List<Notification>

    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.user.userId = :userId AND n.isRead = false")
    fun markAllAsReadByUserId(userId: Long)
}
