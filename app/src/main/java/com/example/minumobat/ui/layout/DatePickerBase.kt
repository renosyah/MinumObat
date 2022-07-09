package com.example.minumobat.ui.layout

import android.content.Context
import com.example.minumobat.model.date_picker_model.DateModel

open class DatePickerBase {
    protected lateinit var ctx: Context

    interface DatePickerCallback {
        fun onDateResults(results : ArrayList<DateModel>)
    }

    interface RangeDatePickerCallback : DatePickerCallback
    interface StepDatePickerCallback : DatePickerCallback
}