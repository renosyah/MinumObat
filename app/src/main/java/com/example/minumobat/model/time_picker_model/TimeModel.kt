package com.example.minumobat.model.time_picker_model


import java.sql.Time
import java.util.Calendar

// kelas untuk data waktu
class TimeModel(var hour: Int = 0,var minute : Int = 0,var second : Int = 0,var mode : String = AM){
    companion object {
        val AM = "AM"
        val PM = "PM"

        // fungsi static untuk konversi dari sql time
        // ke instance time model
        fun fromTime(time : Time?) : TimeModel{
            if (time == null) return TimeModel(0,0,0,"")
            val cal = Calendar.getInstance()
            cal.time = time
            return TimeModel(
                cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), 0 ,
                if (cal.get(Calendar.AM_PM) == Calendar.AM) TimeModel.AM else TimeModel.PM
            )
        }
    }

    // mungsi untuk konversi ke string
    override fun toString() : String {
        return "${String.format("%02d",this.hour)}:${String.format("%02d",this.minute)}"
    }

    // mungsi untuk konversi ke string dengan fortmat pm am
    fun toStringWithPmAm() : String {
        return "${String.format("%02d",this.hour)}:${String.format("%02d",this.minute)}:00 ${this.mode}"
    }

    // fungsi untuk parsing ke bentuk
    // sql time
    fun parseToTime() : Time {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR,this.hour)
        cal.set(Calendar.MINUTE,this.minute)
        cal.set(Calendar.SECOND,this.second)
        cal.set(Calendar.AM_PM,if (this.mode == AM) Calendar.AM else Calendar.PM)
        return Time(cal.time.time)
    }

    // fungsi duplicate instance
    fun duplicate() : TimeModel {
        return TimeModel(
            this.hour, this.minute, this.second, this.mode
        )
    }
}
