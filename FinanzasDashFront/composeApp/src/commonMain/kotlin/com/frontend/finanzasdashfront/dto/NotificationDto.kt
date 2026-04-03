package com.frontend.finanzasdashfront.dto

import kotlinx.serialization.Serializable

@Serializable
data class NotificationDto(
    val id: Int,
    val message: String,
    val link: String?,
    val isRead: Boolean,
    val createdAt: String
)
