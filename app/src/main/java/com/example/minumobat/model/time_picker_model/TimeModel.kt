package com.example.minumobat.model.time_picker_model


import java.sql.Time
import java.util.Calendar

class TimeModel(var hour: Int = 0,var minute : Int = 0,var second : Int = 0,var mode : String = AM){
    companion object {
        val AM = "AM"
        val PM = "PM"

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

    override fun toString() : String {
        return "${String.format("%02d",this.hour)}:${String.format("%02d",this.minute)}"
    }

    fun toStringWithPmAm() : String {
        return "${String.format("%02d",this.hour)}:${String.format("%02d",this.minute)}:00 ${this.mode}"
    }

    fun parseToTime() : Time {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR,this.hour)
        cal.set(Calendar.MINUTE,this.minute)
        cal.set(Calendar.SECOND,this.second)
        cal.set(Calendar.AM_PM,if (this.mode == AM) Calendar.AM else Calendar.PM)
        return Time(cal.time.time)
    }




    fun duplicate() : TimeModel {
        return TimeModel(
            this.hour, this.minute, this.second, this.mode
        )
    }
}
