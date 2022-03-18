package com.example.minumobat.model.time_picker_model

import java.sql.Date
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*

class TimeModel(var hour: Int = 0,var minute : Int = 0,var second : Int = 0,var mode : String = AM){
    companion object {
        val AM = "AM"
        val PM = "PM"
    }

    override fun toString() : String {
        return "${String.format("%02d",hour)}:${String.format("%02d",minute)}"
    }

    fun toStringWithPmAm() : String {
        return "${String.format("%02d",hour)}:${String.format("%02d",minute)}:00 ${mode}"
    }

    fun parseToTime() : Time {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR,hour)
        cal.set(Calendar.MINUTE,minute)
        cal.set(Calendar.SECOND,second)
        cal.set(Calendar.AM_PM,if (mode == AM) Calendar.AM else Calendar.PM)
        return Time(cal.time.time)
    }

    fun duplicate() : TimeModel {
        return TimeModel(
            hour, minute, second, mode
        )
    }
}
