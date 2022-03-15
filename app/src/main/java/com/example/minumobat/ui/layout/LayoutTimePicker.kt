package com.example.minumobat.ui.layout

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.minumobat.R
import com.example.minumobat.model.SimpleModel
import com.example.minumobat.model.time_picker_model.TimeModel
import com.example.minumobat.ui.adapter.TimeAdapter


class LayoutTimePicker {
    private var mode : TimePicker
    private var hour : TimePicker
    private var minute : TimePicker
    private var selectedTime = TimeModel()

    constructor(c: Context, v : View, onScroll : (TimeModel) -> Unit){

        val onModeScroll : (SimpleModel) -> Unit = { t ->
            selectedTime.mode = t.label
            onScroll.invoke(selectedTime)
        }
        val onHourScroll : (SimpleModel) -> Unit = { t ->
            selectedTime.hour = t.value
            onScroll.invoke(selectedTime)
        }
        val onMinuteScroll : (SimpleModel) -> Unit = { t ->
            selectedTime.minute = t.value
            onScroll.invoke(selectedTime)
        }

        val modes = arrayListOf(
            SimpleModel(label = "", value = 0, flag = SimpleModel.FLAG_UNSELECTED),
            SimpleModel(label = TimeModel.AM, value = 0, flag = SimpleModel.FLAG_SELECTED),
            SimpleModel(label = TimeModel.PM, value = 0, flag = SimpleModel.FLAG_SELECTED),
            SimpleModel(label = "", value = 0, flag = SimpleModel.FLAG_UNSELECTED)
        )
        this.mode = TimePicker(c, v.findViewById(R.id.recycleview_mode),modes, TimeAdapter.SHOW_MODE, onModeScroll)

        val hours = ArrayList<SimpleModel>()
        hours.add(SimpleModel(label = "", value = 0, flag = SimpleModel.FLAG_UNSELECTED))
        for (i in 1 until 13) hours.add(SimpleModel(label = "${ i }", value = i, flag = SimpleModel.FLAG_SELECTED))
        hours.add(SimpleModel(label = "", value = 0, flag = SimpleModel.FLAG_UNSELECTED))
        this.hour = TimePicker(c, v.findViewById(R.id.recycleview_hour), hours, TimeAdapter.SHOW_HOUR, onHourScroll)

        val minutes = ArrayList<SimpleModel>()
        minutes.add(SimpleModel(label = "", value = 0, flag = SimpleModel.FLAG_UNSELECTED))
        for (i in 1 until 60) minutes.add(SimpleModel(label = "${ i }", value = i, flag = SimpleModel.FLAG_SELECTED))
        this.minute = TimePicker(c, v.findViewById(R.id.recycleview_minute), minutes, TimeAdapter.SHOW_MINUTE, onMinuteScroll)
        minutes.add(SimpleModel(label = "", value = 0, flag = SimpleModel.FLAG_UNSELECTED))
    }


    private class TimePicker {
        private var items : ArrayList<SimpleModel>
        private var adapter : TimeAdapter
        private var recycleview : RecyclerView

        private fun setAllItemNotSelected(items : ArrayList<SimpleModel>){
            for (i in items) i.flag = SimpleModel.FLAG_UNSELECTED
        }

        constructor(context: Context, recycleview : RecyclerView, items : ArrayList<SimpleModel>, mode : Int, onScroll : (SimpleModel) ->Unit) {
            this.items = items
            this.adapter = TimeAdapter(context, items, mode)

            this.recycleview = recycleview
            this.recycleview.adapter = adapter

            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            this.recycleview.layoutManager = layoutManager

            if (items.size > 0){
                layoutManager.scrollToPositionWithOffset(1, 0)
                setAllItemNotSelected(items)
                items[2].flag = SimpleModel.FLAG_SELECTED
            }

            this.recycleview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val visibleItemCount: Int = layoutManager.getChildCount()
                    val totalItemCount: Int = layoutManager.getItemCount()
                    val firstVisibleItemPosition: Int = layoutManager.findFirstVisibleItemPosition()
                    val lastItem = firstVisibleItemPosition + visibleItemCount

                    setAllItemNotSelected(items)
                    items[(firstVisibleItemPosition + 1)].flag = SimpleModel.FLAG_SELECTED

                    adapter.notifyDataSetChanged()

                    onScroll.invoke(items.get(firstVisibleItemPosition + 1))
                }
            })

            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(this.recycleview)

            this.adapter.notifyDataSetChanged()
        }
    }
}