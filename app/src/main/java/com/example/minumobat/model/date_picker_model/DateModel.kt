package com.example.minumobat.model.date_picker_model

class DateModel(var day : Int = 0, var month : Int = 0, var years : Int = 0, var flag : Int = FLAG_UNSELECTED, var flag_action : Int = FLAG_NONE){
    companion object {
        val FLAG_NONE = 0
        val FLAG_UNSELECTED = 1
        val FLAG_SELECTED = 2
        val FLAG_CURRENT = 3
        val FLAG_UNREACH = 4
    }

    override fun toString() : String{
        return "${day}-${month}-${years}"
    }

    fun duplicate() : DateModel {
        return  DateModel(
            day, month, years, flag, flag_action
        )
    }
}