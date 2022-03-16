package com.example.minumobat.ui.layout

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.minumobat.R
import com.example.minumobat.model.date_picker_model.DateModel
import com.example.minumobat.model.date_picker_model.DatePickerModel
import com.example.minumobat.util.DatePickerUtil
import com.example.range_date_picker.adapter.DateAdapter
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
class LayoutDatePicker {
    private var texview_title : TextView
    private var prev : ImageView
    private var next : ImageView

    private lateinit var dateAdapter : DateAdapter
    private var recycleview_date : RecyclerView

    private var currentDate = LocalDate.now()
    private var datePickerModel = DatePickerUtil.setDateToNow(currentDate)

    private var startDate : DateModel = DateModel()
    private var endDate : DateModel = DateModel()

    constructor(c: Context, v : View , onClick : (DateModel, DateModel) -> Unit)  {
        prev = v.findViewById(R.id.prev)
        prev.setOnClickListener {
            currentDate = currentDate.minusMonths(1)
            datePickerModel = DatePickerUtil.setDateToNow(currentDate)
            refreshLayout(c, datePickerModel)
        }
        next = v.findViewById(R.id.next)
        next.setOnClickListener {
            currentDate = currentDate.plusMonths(1)
            datePickerModel = DatePickerUtil.setDateToNow(currentDate)
            refreshLayout(c, datePickerModel)
        }
        texview_title = v.findViewById(R.id.texview_title)
        recycleview_date = v.findViewById(R.id.recycleview_date)

        setAdapter(c, datePickerModel, onClick)
    }

    private fun refreshLayout(c: Context, d : DatePickerModel){
        texview_title.text = "${DatePickerUtil.getMonthName(c, d.month - 1)} ${d.year}"
        dateAdapter.setItems(d.days)
        restoreSelection(dateAdapter.list)
        dateAdapter.notifyDataSetChanged()
    }

    private fun setAdapter(c: Context, d : DatePickerModel, onClick : (DateModel, DateModel) -> Unit){
        dateAdapter = DateAdapter(c, d.days){ item, pos ->
            if ( ! startDate.isEmpty() && !  endDate.isEmpty()){
                startDate = DateModel()
                endDate = DateModel()
                clearSelected(dateAdapter.list)
            }

            if (startDate.isEmpty() && endDate.isEmpty()){
                startDate = item
                startDate.flag_action = DateModel.FLAG_SELECTED
                dateAdapter.notifyDataSetChanged()
                return@DateAdapter
            }

            if (endDate.isEmpty()){
                endDate = item
                endDate.flag_action = DateModel.FLAG_SELECTED
            }

            if (startDate.isMoreOrEqualThan(endDate)){
                startDate = DateModel()
                endDate = DateModel()
                clearSelected(dateAdapter.list)
                dateAdapter.notifyDataSetChanged()
                return@DateAdapter
            }

            for (i in dateAdapter.list){
                if (i.isMoreOrEqualThan(startDate) && i.isLessOrEqualThan(endDate)){
                   i.flag_action = DateModel.FLAG_SELECTED
                }
            }

            dateAdapter.notifyDataSetChanged()
            onClick.invoke(startDate, endDate)
        }
        recycleview_date.adapter = dateAdapter
        recycleview_date.apply {
            layoutManager = GridLayoutManager(c, 7)
        }

        refreshLayout(c, datePickerModel)
    }

    private fun clearSelected(days : ArrayList<DateModel>){
        for (item in days) item.flag_action = item.flag
    }

    private  fun restoreSelection(days : ArrayList<DateModel>){
        if ( ! startDate.isEmpty() &&  ! endDate.isEmpty()){
            for (i in days){
                if (i.isMoreOrEqualThan(startDate) && i.isLessOrEqualThan(endDate)){
                    i.flag_action = DateModel.FLAG_SELECTED
                }
            }
        }
    }
}