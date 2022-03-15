package com.example.minumobat.util

import com.example.minumobat.BuildConfig
import android.app.NotificationManager
import android.app.ActivityManager
import android.content.Context


class Utils {
    companion object {
        val NOTIF_CHANNEL_ID: String = BuildConfig.APPLICATION_ID.toString() + "_NOTIFICATION_ID"
        val NOTIF_CHANNEL_NAME: String = BuildConfig.APPLICATION_ID.toString() + "_NOTIFICATION_NAME"
        val NOTIF_CHANNEL_DES: String = BuildConfig.APPLICATION_ID.toString() + "_NOTIFICATION_DES"
        val importance = NotificationManager.IMPORTANCE_HIGH

        fun isMyServiceRunning(c: Context, s: Class<*>): Boolean {
            val manager = (c.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
            for (service in manager.getRunningServices(Int.MAX_VALUE)) {
                if (s.name == service.service.className) {
                    return true
                }
            }
            return false
        }
    }
}