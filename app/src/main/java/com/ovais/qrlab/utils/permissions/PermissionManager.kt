package com.ovais.qrlab.utils.permissions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

interface PermissionManager {
    val hasCameraPermission: Boolean
    val hasStoragePermission: Boolean
    val storagePermission: String
    val cameraPermission: String
    val hasNotificationPermission: Boolean
    val notificationPermission: String
}

class DefaultPermissionManager(
    private val context: Context
) : PermissionManager {
    override val hasCameraPermission: Boolean
        get() = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

    override val hasStoragePermission: Boolean
        get() {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // Android 13+ requires READ_MEDIA_IMAGES
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_MEDIA_IMAGES
                ) == PackageManager.PERMISSION_GRANTED
            } else {
                // Android 12 and below use READ_EXTERNAL_STORAGE
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            }
        }
    override val storagePermission: String
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
    override val cameraPermission: String
        get() = Manifest.permission.CAMERA
    override val hasNotificationPermission: Boolean
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                notificationPermission
            ) == PackageManager.PERMISSION_GRANTED
        } else true
    override val notificationPermission: String
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        get() = Manifest.permission.POST_NOTIFICATIONS
}