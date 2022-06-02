package com.example.minumobat.util

import android.app.ActivityManager
import android.app.NotificationManager
import android.content.Context
import com.example.minumobat.BuildConfig
import com.example.minumobat.model.time_picker_model.TimeModel
import java.sql.Date
import java.sql.Time
import java.util.*


class Utils {
    companion object {
        val NAME_MORNING = "MORNING"
        val NAME_AFTERNOON = "AFTERNOON"
        val NAME_NIGHT = "NIGHT"

        // range waktu pagi
        fun MORNING_RANGE_START() : Time {
            val cal = Calendar.getInstance()
            cal.set(Calendar.HOUR_OF_DAY,0)
            cal.set(Calendar.MINUTE,1)
            cal.set(Calendar.SECOND,0)
            return Time(cal.time.time)
        }
        fun MORNING_RANGE_END() : Time {
            val cal = Calendar.getInstance()
            cal.set(Calendar.HOUR_OF_DAY,9)
            cal.set(Calendar.MINUTE,59)
            cal.set(Calendar.SECOND,0)
            return Time(cal.time.time)
        }

        // range waktu siang
        fun AFTERNOON_RANGE_START() : Time {
            val cal = Calendar.getInstance()
            cal.set(Calendar.HOUR_OF_DAY,10)
            cal.set(Calendar.MINUTE,0)
            cal.set(Calendar.SECOND,0)
            return Time(cal.time.time)
        }
        fun AFTERNOON_RANGE_END() : Time {
            val cal = Calendar.getInstance()
            cal.set(Calendar.HOUR_OF_DAY,14)
            cal.set(Calendar.MINUTE,59)
            cal.set(Calendar.SECOND,0)
            return Time(cal.time.time)
        }

        // range waktu malam
        fun NIGHT_RANGE_START() : Time {
            val cal = Calendar.getInstance()
            cal.set(Calendar.HOUR_OF_DAY,15)
            cal.set(Calendar.MINUTE,0)
            cal.set(Calendar.SECOND,0)
            return Time(cal.time.time)
        }
        fun NIGHT_RANGE_END() : Time {
            val cal = Calendar.getInstance()
            cal.set(Calendar.HOUR_OF_DAY,23)
            cal.set(Calendar.MINUTE,59)
            cal.set(Calendar.SECOND,0)
            return Time(cal.time.time)
        }

        // fungsi untuk menetukan apakah
        // waktu berada di dalam range
        // waktu yang telah di tentukan
        fun isBetween(name : String, current : TimeModel) : Boolean {
            val now = Time(Calendar.getInstance().time.time)
            //Log.e("now", "${now}  ${current.parseToTime()}")

            when (name){
                NAME_MORNING -> {
                    return current.parseToTime().after(MORNING_RANGE_START()) && current.parseToTime().before(MORNING_RANGE_END())
                            && now.after(MORNING_RANGE_START()) && now.before(MORNING_RANGE_END())
                }
                NAME_AFTERNOON -> {
                    return current.parseToTime().after(AFTERNOON_RANGE_START()) && current.parseToTime().before(AFTERNOON_RANGE_END())
                            && now.after(AFTERNOON_RANGE_START()) && now.before(AFTERNOON_RANGE_END())
                }
                NAME_NIGHT -> {
                    return current.parseToTime().after(NIGHT_RANGE_START()) && current.parseToTime().before(NIGHT_RANGE_END())
                            && now.after(NIGHT_RANGE_START()) && now.before(NIGHT_RANGE_END())
                }
            }
            return false
        }

        val NOTIF_CHANNEL_ID: String = BuildConfig.APPLICATION_ID.toString() + "_NOTIFICATION_ID"
        val NOTIF_CHANNEL_NAME: String = BuildConfig.APPLICATION_ID.toString() + "_NOTIFICATION_NAME"
        val NOTIF_CHANNEL_DES: String = BuildConfig.APPLICATION_ID.toString() + "_NOTIFICATION_DES"
        val importance = NotificationManager.IMPORTANCE_HIGH

        // fungsi untuk mengecheck
        // pakah sercive app sedang berjalan
        // atau terhenti
        fun isMyServiceRunning(c: Context, s: Class<*>): Boolean {
            val manager = (c.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
            for (service in manager.getRunningServices(Int.MAX_VALUE)) {
                if (s.name == service.service.className) {
                    return true
                }
            }
            return false
        }

        fun getDatesFromToEnd(date1: Date, date2: Date): List<Date> {
            val dates = ArrayList<Date>()

            val cal1 = Calendar.getInstance()
            cal1.time = date1

            val cal2 = Calendar.getInstance()
            cal2.time = date2

            while (! cal1.after(cal2)) {
                dates.add(Date(cal1.time.time))
                cal1.add(Calendar.DATE, 1)
            }
            return dates
        }
    }
}