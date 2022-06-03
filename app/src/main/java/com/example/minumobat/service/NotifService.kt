package com.example.minumobat.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.minumobat.BuildConfig
import com.example.minumobat.R
import com.example.minumobat.service.AppReceiver.Companion.ACTION_RESTART_SERVICE
import com.example.minumobat.ui.activity.notification_splash.NotificationSplashActivity
import com.example.minumobat.util.Utils.Companion.NOTIF_CHANNEL_DES
import com.example.minumobat.util.Utils.Companion.NOTIF_CHANNEL_ID
import com.example.minumobat.util.Utils.Companion.NOTIF_CHANNEL_NAME
import kotlin.random.Random
import android.content.IntentFilter
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.*
import com.example.minumobat.model.date_picker_model.DateModel
import com.example.minumobat.model.schedule_model.ScheduleModel
import com.example.minumobat.model.schedule_model.ScheduleViewModel
import com.example.minumobat.model.time_picker_model.TimeModel
import com.example.minumobat.ui.activity.schedule_page.SchedulePageActivity
import java.sql.Date
import java.sql.Time
import java.util.Calendar

// kelas notfikasi service
// adalah kelas service yang akan berjalan
// pararel dengan aplikasi
// namun tidak satu proses
class NotifService : LifecycleService() {
    companion object {
        val ACTION_CHANGE_TIME_SCHEDULE_FOR_NOTIFICATION = "android.intent.action.ACTION_EXPIRED_CHANGE_TIME_SCHEDULE_FOR_NOTIFICATION"
    }

    lateinit var context: Context
    private var lifecycleOwner: LifecycleOwner? = null
    private var timeChangedReceiver: BroadcastReceiver? = null
    private val s_intentFilter = IntentFilter()

    lateinit var scheduleViewModel: ScheduleViewModel

    // fungsi pada saat
    // instance service
    // dibuat
    override fun onCreate() {
        super.onCreate()

        // inisalisasi intance
        context = this@NotifService
        lifecycleOwner = this@NotifService
        scheduleViewModel = ScheduleViewModel(application)

        // mulai foreground service
        // jika setting project ENABLE_FOREGROUND di set true
        if (BuildConfig.ENABLE_FOREGROUND) {
            startForeground()
        }

        // tambahkan intent filter
        // yang mana : intent untuk setiap menit berganti
        s_intentFilter.addAction(ACTION_CHANGE_TIME_SCHEDULE_FOR_NOTIFICATION)
        s_intentFilter.addAction(Intent.ACTION_TIME_TICK)

        // inisialisasi broadcast receiver
        // untuk menghandle setiap menit yang berganti
        timeChangedReceiver = object : BroadcastReceiver() {
            override fun onReceive(p0: Context, intent: Intent?) {
                if (intent == null){
                    return
                }

                // jika perintah adalah menit yang berganti
                if (intent.action == Intent.ACTION_TIME_TICK){

                    // query schedule dengan tanggal saat ini
                    // untuk yang tipe obat regular
                    scheduleViewModel.getCurrent(Date(Calendar.getInstance().time.time), object : MutableLiveData<List<ScheduleModel>>() {
                        override fun setValue(value: List<ScheduleModel>) {
                            super.setValue(value)

                            // stop jika data kosong
                            if (value.isEmpty()){
                                return
                            }

                            // iterasi dengan loop untuk
                            // setiap data yang berhasil di query
                            for (i in value){

                                // jika status di off atau waktu tidak valid
                                // lanjutkan ke iterasi selanjutnya
                                if (i.status == ScheduleModel.STATUS_OFF) continue
                                if (i.time == null) continue

                                // check type obat
                                // apakah suntik
                                // atau reguler
                                when (i.typeMedicine){
                                    ScheduleModel.TYPE_INJECTION_MEDICINE -> {
                                        if (scheduleTypeMedicineInject(i)){
                                            return
                                        }
                                    }
                                    ScheduleModel.TYPE_REGULAR_MEDICINE -> {
                                        if (scheduleTypeMedicineRegular(i)){
                                            return
                                        }
                                    }
                                }

                            }
                        }
                    })
                }
            }
        }

        // registrasi broadcast receiver
        registerReceiver(timeChangedReceiver, s_intentFilter)
    }

    // fungsi untuk menghandle saat service
    // dimulai, dan mngeset status dan kelakuan service
    // dalam kasus ini, service akan dibiarkan menyala
    // selama aplikasi tidak di stop oleh user
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        if (timeChangedReceiver != null) {
            registerReceiver(timeChangedReceiver, s_intentFilter)
        }
        return START_STICKY
    }

    // pada saat service dihancurkan
    // hilangkan registrasi broadcast receiver
    override fun onDestroy() {
        super.onDestroy()
        try {
            unregisterReceiver(timeChangedReceiver)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }

    // saat ini tidak digunakan
    // karna service independen dari
    // aplikasi
    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }

    // pada saat aplikasi di swipe
    // restart service
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

    // fungsi untuk menampilkan notifikasi
    // foreground service
    // yang mana akan muncul notifkasi yang tidak bisa
    // dihilangkan, tujuan untuj memberi tau user
    // app sedang berjalan
    private fun startForeground() {
        val channel = NotificationChannel(
            NOTIF_CHANNEL_ID.toString() + "_FOREGROUND",
            NOTIF_CHANNEL_NAME.toString() + "_FOREGROUND",
            NotificationManager.IMPORTANCE_LOW
        )
        channel.description = NOTIF_CHANNEL_DES.toString() + "_FOREGROUND"
        channel.setShowBadge(false)

        val notificationManager by lazy { NotificationManagerCompat.from(context) }
        notificationManager.createNotificationChannel(channel)

        val fullScreenIntent = Intent(context, SchedulePageActivity::class.java)
        val fullScreenPendingIntent = PendingIntent.getActivity(context, 0, fullScreenIntent, 0)

        val notification = NotificationCompat.Builder(context, NOTIF_CHANNEL_ID.toString() + "_FOREGROUND")
                .setSmallIcon(R.drawable.icon)
                .setContentIntent(fullScreenPendingIntent)
                .setContentText(getText(R.string.foreground_notification_message))
                .build()

        startForeground(ONGOING_NOTIFICATION_ID, notification)
    }

    // fungsi untuk memunculkan notifikasi
    // yang mana saat dilik akan diarahkan
    // ke halama splash notifikasi
    private fun sendNotification(ctx : Context, description : String, time : String, typeMedicine : Int) {
        val mapIntent = NotificationSplashActivity.createIntent(ctx, description, time, typeMedicine)
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
                .setContentIntent(pendingIntent)
                .setFullScreenIntent(pendingIntent, true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setSound(defaultSoundUri)

        val id: Int = Random(System.currentTimeMillis()).nextInt(100)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)
        notificationManager.notify(id, notificationBuilder.build())
    }

    // handle jika schedule tipe obat reguler
    private fun scheduleTypeMedicineRegular(i: ScheduleModel) : Boolean {

        // before 60 minute
        var currrentTime = getCurrentTime(60)
        Log.e("60 minute", "${i.time} ${currrentTime}")

        // jika sesuai waktu sekarang + 60 menit
        // apakah sesai dengan waktu yang ingin dinotif
        // kirimkan notifikasi
        if (isMatch(i.time!!,currrentTime)){
            sendNotification(context, context.getString(R.string.six_ten_minute), TimeModel.fromTime(i.time).toString(), i.typeMedicine)
            return true
        }

        // jika sesuai waktu sekarang + 15 menit
        // apakah sesai dengan waktu yang ingin dinotif
        // kirimkan notifikasi
        currrentTime = getCurrentTime(15)
        Log.e("15 minute", "${i.time} ${currrentTime}")

        if (isMatch(i.time!!,currrentTime)){
            sendNotification(context,context.getString(R.string.five_ten_minute), TimeModel.fromTime(i.time).toString(), i.typeMedicine)
            return true
        }

        // jika sesuai waktu sekarang
        // apakah sesai dengan waktu yang ingin dinotif
        // kirimkan notifikasi
        currrentTime = getCurrentTime(0)
        Log.e("on time", "${i.time} ${currrentTime}")
        Log.e("----", "----")

        if (isMatch(i.time!!,currrentTime)){
            sendNotification(context, i.description, TimeModel.fromTime(i.time).toString(), i.typeMedicine)
            return true
        }


        return false
    }

    // handle jika schedule tipe obat suntik
    private fun scheduleTypeMedicineInject(i: ScheduleModel) : Boolean {
        return false
    }


    // fungsi untuk mendapatkan waktu saat ini
    // tampa detik dan mili detik
    private fun getCurrentTime(addMinute : Int) : Time {
        val current = Calendar.getInstance()
        current.add(Calendar.MINUTE, addMinute)
        current.set(Calendar.SECOND, 0)
        return Time(current.time.time)
    }

    // apakah waktu sesuai dengan
    // mengkomparasi di bentuk string
    private fun isMatch(t1: Time, t2 : Time) : Boolean {
        return "$t1" == "$t2"
    }
}