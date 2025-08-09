package com.ovais.quickcode.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.ovais.quickcode.R

interface NotificationManager {
    fun showNotification(title: String?, body: String?)
    fun showExportNotification(fileName: List<String>)
    fun showExportError(error: String?)
}

class DefaultNotificationManager(
    private val context: Context
) : NotificationManager {

    @SuppressLint("ServiceCast")
    override fun showNotification(title: String?, body: String?) {
        val channelId = context.getString(R.string.default_channel_id)
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Default Channel",
                android.app.NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.app_icon_playstore)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)

        notificationManager.notify(0, notificationBuilder.build())
    }

    override fun showExportNotification(fileName: List<String>) {
        val channelId = context.getString(R.string.export_channel_id)
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    channelId,
                    context.getString(R.string.export_text),
                    android.app.NotificationManager.IMPORTANCE_DEFAULT
                )
            )
        }
        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle(context.getString(R.string.export_title))
            .setContentText(context.getString(R.string.export_message, fileName.joinToString()))
            .setSmallIcon(android.R.drawable.stat_sys_download_done)
            .build()

        notificationManager.notify(1, notification)
    }

    override fun showExportError(error: String?) {
        val channelId = context.getString(R.string.export_channel_id)
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    channelId,
                    context.getString(R.string.export_text),
                    android.app.NotificationManager.IMPORTANCE_DEFAULT
                )
            )
        }
        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle(context.getString(R.string.error))
            .setContentText(error)
            .setSmallIcon(android.R.drawable.stat_sys_download_done)
            .build()

        notificationManager.notify(1, notification)
    }
}