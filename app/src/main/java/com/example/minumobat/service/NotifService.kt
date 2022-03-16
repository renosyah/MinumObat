package com.example.minumobat.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleService
import com.example.minumobat.BuildConfig
import com.example.minumobat.R
import com.example.minumobat.service.AppReceiver.Companion.ACTION_RESTART_SERVICE
import com.example.minumobat.ui.activity.notification_splash.NotificationSplashActivity
import com.example.minumobat.util.Utils.Companion.NOTIF_CHANNEL_DES
import com.example.minumobat.util.Utils.Companion.NOTIF_CHANNEL_ID
import com.example.minumobat.util.Utils.Companion.NOTIF_CHANNEL_NAME
import kotlin.random.Random
import android.content.IntentFilter

class NotifService : LifecycleService() {

    companion object {
        val ACTION_CHANGE_TIME_SCHEDULE_FOR_NOTIFICATION = "android.intent.action.ACTION_EXPIRED_CHANGE_TIME_SCHEDULE_FOR_NOTIFICATION"
    }

    lateinit var context: Context
    private var lifecycleOwner: LifecycleOwner? = null
    private var timeChangedReceiver: BroadcastReceiver? = null
    private val s_intentFilter = IntentFilter()

    override fun onCreate() {
        super.onCreate()

        context = this
        lifecycleOwner = this

        if (BuildConfig.ENABLE_FOREGROUND) {
            startForeground()
        }

        s_intentFilter.addAction(ACTION_CHANGE_TIME_SCHEDULE_FOR_NOTIFICATION)
        s_intentFilter.addAction(Intent.ACTION_TIME_TICK)

        timeChangedReceiver = object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, intent: Intent?) {
                if (intent == null){
                    return
                }

                if (intent.action == Intent.ACTION_TIME_TICK){

                    // query check if any schedule
                    // testing
                    sendNotification(context, "1 minute hass pass!")
                }
            }
        }
        registerReceiver(timeChangedReceiver, s_intentFilter)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        if (timeChangedReceiver != null){
            registerReceiver(timeChangedReceiver, s_intentFilter)
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            unregisterReceiver(timeChangedReceiver)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        try {
            unregisterReceiver(timeChangedReceiver)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }

        val i = Intent(ACTION_RESTART_SERVICE)
        i.setClass(baseContext, AppReceiver::class.java)
        sendBroadcast(i)
    }

    private val ONGOING_NOTIFICATION_ID: Int = Random(System.currentTimeMillis()).nextInt(100)

    private fun startForeground() {
        val channel = NotificationChannel(
            NOTIF_CHANNEL_ID.toString() + "_FOREGROUND",
            NOTIF_CHANNEL_NAME.toString() + "_FOREGROUND",
            NotificationManager.IMPORTANCE_LOW
        )
        channel.description = NOTIF_CHANNEL_DES.toString() + "_FOREGROUND"
        channel.setShowBadge(false)
        val notificationManager = getSystemService(
            NotificationManager::class.java
        )
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel)
            val notification: Notification = NotificationCompat.Builder(context, NOTIF_CHANNEL_ID.toString() + "_FOREGROUND")
                    .setSmallIcon(R.drawable.icon)
                    .setContentText(getText(R.string.foreground_notification_message))
                    .build()

            startForeground(ONGOING_NOTIFICATION_ID, notification)
        }
    }

    private fun sendNotification(ctx : Context, description : String) {
        val mapIntent = Intent(ctx, NotificationSplashActivity::class.java)
        mapIntent.putExtra("description", description)
        mapIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        mapIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        mapIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val pendingIntent = PendingIntent.getActivity(ctx, 0, mapIntent, PendingIntent.FLAG_ONE_SHOT)

        val channelId = BuildConfig.APPLICATION_ID + ".NOTIFICATION_CHANNEL"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(ctx, channelId)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(description)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)

        val id: Int = Random(System.currentTimeMillis()).nextInt(100)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)
        notificationManager.notify(id, notificationBuilder.build())
    }
}