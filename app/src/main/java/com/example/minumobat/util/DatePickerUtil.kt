package com.example.minumobat.util

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.minumobat.model.date_picker_model.DateModel
import com.example.minumobat.model.date_picker_model.DatePickerModel
import java.time.LocalDate
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
class DatePickerUtil {
   companion object {
       fun setDateToNow(selectedDate: LocalDate): DatePickerModel {
           val d = DatePickerModel()
           val today = LocalDate.now()
           d.year = selectedDate.year
           d.month = selectedDate.monthValue

           val yearMonth: YearMonth = YearMonth.from(selectedDate)
           val daysInMonth: Int = yearMonth.lengthOfMonth()
           val firstOfMonth: LocalDate = selectedDate.withDayOfMonth(1)
           val dayOfWeek: Int = firstOfMonth.getDayOfWeek().getValue()

           for (i in 1..42) {
               when {
                   (i <= dayOfWeek) -> {
                       val _db = selectedDate.minusMonths(1)
                       d.days.add(DateModel(day = 0, month = _db.monthValue, years = _db.year, flag = DateModel.FLAG_UNREACH))
                   }
                   (i > daysInMonth + dayOfWeek) -> {
                       val _da = selectedDate.plusMonths(1)
                       d.days.add(DateModel(day = 1,month = _da.monthValue, years = _da.year, flag = DateModel.FLAG_UNREACH))
                   }
                   else -> {
                       val _td = DateModel(day = i - dayOfWeek, month = d.month , years = d.year)
                       val _nd = DateModel(day = today.dayOfMonth, month = today.monthValue , years = today.year)
                       _td.flag = checkIfDateIsToday(_td,_nd)
                       d.days.add(_td)
                   }
               }
           }
           val prevMonth = getDateFromMonth(selectedDate.minusMonths(1))
           var pos = prevMonth.size - 1
           for (i in d.days.size - 1 downTo 0){
               if (d.days[i].day == 0 && d.days[i].flag == DateModel.FLAG_UNREACH){
                   d.days[i].day = prevMonth[pos]
                   pos--
               }
           }

           val nextMonth = getDateFromMonth(selectedDate.plusMonths(1))
           pos = 0
           for (i in 0 until d.days.size){
               if (d.days[i].day == 1 && d.days[i].flag == DateModel.FLAG_UNREACH){
                   d.days[i].day = nextMonth[pos]
                   pos++
               }
           }
           return d
       }

       private fun checkIfDateIsToday(d : DateModel, today : DateModel) : Int{
           if (d.toString() == today.toString()) return DateModel.FLAG_CURRENT
           return DateModel.FLAG_UNSELECTED
       }

       private fun getDateFromMonth(date: LocalDate): ArrayList<Int>{
           val arr = ArrayList<Int>()

           val yearMonth: YearMonth = YearMonth.from(date)
           val daysInMonth: Int = yearMonth.lengthOfMonth()
           val firstOfMonth: LocalDate = date.withDayOfMonth(1)
           val dayOfWeek: Int = firstOfMonth.getDayOfWeek().getValue()

           for (i in 1..42) {
               if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                   // ignore this wallaby
               } else {
                   arr.add(i - dayOfWeek)
               }
           }
           return  arr
       }

       fun getMonthName(month: Int): String? {
           return when (month) {
               0 -> "January"
               1 -> "February"
               2 -> "March"
               3 -> "April"
               4 -> "May"
               5 -> "June"
               6 -> "July"
               7 -> "August"
               8 -> "September"
               9 -> "October"
               10 -> "November"
               11 -> "December"
               else -> ""
           }
       }
   }
}