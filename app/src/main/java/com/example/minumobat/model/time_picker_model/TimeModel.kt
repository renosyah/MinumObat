package com.example.minumobat.model.time_picker_model

class TimeModel(var hour: Int = 0,var minute : Int = 0,var second : Int = 0,var mode : String = AM){
    companion object {
        val AM = "AM"
        val PM = "PM"
    }

    override fun toString() : String{
        return "${String.format("%02d",hour)}:${String.format("%02d",minute)}"
    }

    fun duplicate() : TimeModel {
        return TimeModel(
            hour, minute, second, mode
        )
    }
}
