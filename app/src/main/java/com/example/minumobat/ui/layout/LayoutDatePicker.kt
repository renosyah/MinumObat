package com.example.minumobat.ui.layout

import android.content.Context
import android.os.Build
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
    private var selectedDate = DateModel()

    constructor(c: Context, v : View , onClick : (DateModel) -> Unit)  {
        prev = v.findViewById(R.id.prev)
        prev.setOnClickListener {
            currentDate = currentDate.minusMonths(1)
            datePickerModel = DatePickerUtil.setDateToNow(currentDate)
            refreshLayout(datePickerModel)
            restoreSelected()
            dateAdapter.notifyDataSetChanged()
        }

        next = v.findViewById(R.id.next)
        next.setOnClickListener {
            currentDate = currentDate.plusMonths(1)
            datePickerModel = DatePickerUtil.setDateToNow(currentDate)
            refreshLayout(datePickerModel)
            restoreSelected()
            dateAdapter.notifyDataSetChanged()
        }

        texview_title = v.findViewById(R.id.texview_title)
        recycleview_date = v.findViewById(R.id.recycleview_date)

        setAdapter(c, datePickerModel, onClick)
    }

    private fun refreshLayout(d : DatePickerModel){
        texview_title.text = "${DatePickerUtil.getMonthName(d.month - 1)} ${d.year}"
        dateAdapter.setItems(d.days)
    }

    private fun setAdapter(c: Context, d : DatePickerModel, onClick : (DateModel) -> Unit){
        dateAdapter = DateAdapter(c,d.days){ item ->
            clearSelected()
            item.flag_action = DateModel.FLAG_SELECTED
            dateAdapter.notifyDataSetChanged()
            selectedDate = item.duplicate()
            onClick.invoke(selectedDate)
        }
        recycleview_date.adapter = dateAdapter
        recycleview_date.apply {
            layoutManager = GridLayoutManager(c, 7)
        }

        refreshLayout(datePickerModel)
    }

    private fun clearSelected(){
        for (item in datePickerModel.days) item.flag_action = item.flag
    }

    private fun restoreSelected(){
        clearSelected()
        for (item in datePickerModel.days) {
            if (item.toString() == selectedDate.toString()) {
                item.flag_action = DateModel.FLAG_SELECTED
                break
            }
        }
    }
}