package com.connectdeaf.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.ui.graphics.vector.ImageVector

data class Notification(
    val title: String,
    val message: String,
    val time: String,
    val icon: ImageVector = Icons.Default.Notifications
)