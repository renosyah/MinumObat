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
        return "${String.format("%02d",hour)}:${String.format("%02d",minute)} ${mode}"
    }

    fun parseToTime() : Time {
        return Time(SimpleDateFormat("hh:mm:ss a").parse(toStringWithPmAm()).time)
    }

    fun duplicate() : TimeModel {
        return TimeModel(
            hour, minute, second, mode
        )
    }
}
