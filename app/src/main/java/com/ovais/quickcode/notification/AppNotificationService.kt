package com.ovais.quickcode.notification

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber

class AppNotificationService : FirebaseMessagingService() {

    private val notificationManager by lazy {
        DefaultNotificationManager(this.baseContext)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        notificationManager.showNotification(
            title = message.notification?.title,
            body = message.notification?.body
        )
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.i("New Token:$token")
    }
}