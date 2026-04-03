package com.finanzas.dash.finanzas.dto

import java.time.OffsetDateTime

data class NotificationDto(
    val id: Int,
    val message: String,
    val link: String?,
    val isRead: Boolean,
    val createdAt: OffsetDateTime
)
