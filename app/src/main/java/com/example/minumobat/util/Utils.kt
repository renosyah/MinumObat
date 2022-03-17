package com.example.minumobat.util

import com.example.minumobat.BuildConfig
import android.app.NotificationManager
import android.app.ActivityManager
import android.content.Context
import android.util.Log
import com.example.minumobat.model.time_picker_model.TimeModel
import java.util.*


class Utils {
    companion object {
        val NAME_MORNING = "MORNING"
        val NAME_AFTERNOON = "AFTERNOON"
        val NAME_NIGHT = "NIGHT"

        val MORNING_RANGE_START = TimeModel(0,1,0, TimeModel.AM).parseToTime()
        val MORNING_RANGE_END = TimeModel(9,59,0, TimeModel.PM).parseToTime()

        val AFTERNOON_RANGE_START = TimeModel(10,0,0, TimeModel.AM).parseToTime()
        val AFTERNOON_RANGE_END = TimeModel(2,59,0, TimeModel.PM).parseToTime()

        val NIGHT_RANGE_START = TimeModel(3,0,0, TimeModel.PM).parseToTime()
        val NIGHT_RANGE_END = TimeModel(11,59,0, TimeModel.PM).parseToTime()

        fun isBetween(name : String, current : TimeModel) : Boolean {
            val cal = Calendar.getInstance()
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)

            val now = TimeModel(
                cal.get(Calendar.HOUR),
                cal.get(Calendar.MINUTE),
                0,
                if (cal.get(Calendar.AM_PM) == Calendar.PM) TimeModel.PM else TimeModel.AM
            ).parseToTime()

            Log.e("now", "${now}  ${current.parseToTime()}")
            when (name){
                NAME_MORNING -> {
                    return current.parseToTime().after(MORNING_RANGE_START) && current.parseToTime().before(MORNING_RANGE_END)
                            && now.after(MORNING_RANGE_START) && now.before(MORNING_RANGE_END)
                }
                NAME_AFTERNOON -> {
                    return current.parseToTime().after(AFTERNOON_RANGE_START) && current.parseToTime().before(AFTERNOON_RANGE_END)
                            && now.after(AFTERNOON_RANGE_START) && now.before(AFTERNOON_RANGE_END)
                }
                NAME_NIGHT -> {
                    return current.parseToTime().after(NIGHT_RANGE_START) && current.parseToTime().before(NIGHT_RANGE_END)
                            && now.after(NIGHT_RANGE_START) && now.before(NIGHT_RANGE_END)
                }
            }
            return false
        }

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