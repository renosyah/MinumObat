package com.example.minumobat.model.date_picker_model

import java.sql.Date
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

// class untuk format tanggal
class DateModel(var day : Int = 0, var month : Int = 0, var years : Int = 0, var flag : Int = FLAG_UNSELECTED, var flag_action : Int = FLAG_NONE){
    companion object {
        val FLAG_NONE = 0
        val FLAG_UNSELECTED = 1
        val FLAG_SELECTED = 2
        val FLAG_CURRENT = 3
        val FLAG_UNREACH = 4
        val FLAG_NOT_AVALIABLE = 5

        fun parseFromDate(d:Date) : DateModel{
            val cal: Calendar = Calendar.getInstance()
            cal.time = d
            return DateModel(cal.get(Calendar.DAY_OF_MONTH),cal.get(Calendar.MONTH) + 1,cal.get(Calendar.YEAR))
        }
    }

    // koneversi ke string
    override fun toString() : String{
        return "${String.format("%02d",day)}/${String.format("%02d",month)}/${years}"
    }

    // jika tanggal kosong
    fun isEmpty() : Boolean {
        return day == 0 && month == 0 && years == 0
    }

    // duplikasi instance
    fun duplicate() : DateModel {
        return  DateModel(
            day, month, years, flag, flag_action
        )
    }

    // fungsi untuk parsing ke bentuk sql Date
    fun parseToDate() : Date{
        val cal: Calendar = Calendar.getInstance()
        cal.set(Calendar.YEAR, this.years)
        cal.set(Calendar.MONTH, this.month - 1)
        cal.set(Calendar.DAY_OF_MONTH, this.day)
        return Date(cal.time.time)
    }

    // fungsi komparasi untuk menentukan
    // apakah kurang dari tanggal
    // inputan di parameter fungsi
    fun isMoreOrEqualThan(date : DateModel) : Boolean {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val firstDate= LocalDate.parse(date.toString(), formatter)
        val secondDate = LocalDate.parse(this.toString(), formatter)
        return secondDate >= firstDate
    }

    // fungsi komparasi untuk menentukan
    // apakah lebih dari tanggal
    // inputan di parameter fungsi
    fun isLessOrEqualThan(date : DateModel) : Boolean {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val firstDate= LocalDate.parse(date.toString(), formatter)
        val secondDate = LocalDate.parse(this.toString(), formatter)
        return secondDate <= firstDate
    }
}