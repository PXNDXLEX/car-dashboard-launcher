package com.alex.carlauncher

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.app.Notification

class NotificationListener : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        if (sbn.packageName.contains("spotify") || 
            sbn.packageName.contains("youtube.music") || 
            sbn.packageName.contains("google.android.music")) {
            
            val extras = sbn.notification.extras
            val title = extras.getString(Notification.EXTRA_TITLE) ?: "Desconocido"
            val artist = extras.getString(Notification.EXTRA_TEXT) ?: "—"

            // Aquí puedes actualizar el widget de música en el dashboard
            // Por ahora solo mostramos en Log (más adelante lo conectamos al UI)
            android.util.Log.d("MUSIC", "🎵 $title - $artist")
        }
    }
}