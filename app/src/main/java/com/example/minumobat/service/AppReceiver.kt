package com.example.minumobat.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.minumobat.util.Utils.Companion.isMyServiceRunning


class AppReceiver : BroadcastReceiver() {
    companion object {
        val ACTION_COMMON_MESSAGE = "action.common.message"
        val ACTION_RESTART_SERVICE = "action.restart.service"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action.toString()
        val intents = arrayListOf<String>(Intent.ACTION_BOOT_COMPLETED, Intent.ACTION_DATE_CHANGED, Intent.ACTION_TIME_CHANGED, Intent.ACTION_TIMEZONE_CHANGED)

        if (action == ACTION_RESTART_SERVICE) {
            val i = Intent(context, NotifService::class.java)
            context.stopService(i)
            context.startService(i)

        } else if (action in intents) {
            val i = Intent(context, NotifService::class.java)
            if (!isMyServiceRunning(context, NotifService::class.java)) {
                context.startForegroundService(i)
            }
        }
    }
}