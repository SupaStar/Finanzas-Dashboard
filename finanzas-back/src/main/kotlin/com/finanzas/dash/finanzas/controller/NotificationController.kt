package com.finanzas.dash.finanzas.controller

import com.finanzas.dash.finanzas.dto.NotificationDto
import com.finanzas.dash.finanzas.service.NotificationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/notifications")
class NotificationController(
    private val notificationService: NotificationService
) {

    @GetMapping("/unread")
    fun getUnreadNotifications(principal: Principal): ResponseEntity<List<NotificationDto>> {
        val notifications = notificationService.getUnreadNotifications(principal.name)
        return ResponseEntity.ok(notifications)
    }

    @PostMapping("/{id}/read")
    fun markAsRead(@PathVariable id: Int, principal: Principal): ResponseEntity<Void> {
        notificationService.markAsRead(id, principal.name)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/read-all")
    fun markAllAsRead(principal: Principal): ResponseEntity<Void> {
        notificationService.markAllAsRead(principal.name)
        return ResponseEntity.ok().build()
    }
}
