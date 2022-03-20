package com.example.minumobat.ui.layout

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.minumobat.R
import com.example.minumobat.model.SimpleModel
import com.example.minumobat.model.time_picker_model.TimeModel
import com.example.range_date_picker.adapter.TimeAdapter


// class untuk layout time picker
// class ini yang akan menghandle proses
// untuk tampilan time picker
class LayoutTimePicker {
    lateinit var description : TextView
    lateinit var descriptionButton : TextView

    private var mode : TimePicker
    private var hour : TimePicker
    private var minute : TimePicker
    var selectedTime = TimeModel()

    // kontruktor kelas
    constructor(c: Context, v : View, rangeHour : ArrayList<Int>, onScroll : (TimeModel) -> Unit, onDescriptionClick : () -> Unit){
        description = v.findViewById(R.id.description_text)
        descriptionButton = v.findViewById(R.id.description_button)
        descriptionButton.setOnClickListener {
            onDescriptionClick.invoke()
        }

        // pada saat user scroll mode am pm
        val onModeScroll : (SimpleModel) -> Unit = { t ->
            selectedTime.mode = t.label
            onScroll.invoke(selectedTime)
        }

        // pada saat user scroll jam
        val onHourScroll : (SimpleModel) -> Unit = { t ->
            selectedTime.hour = t.value
            onScroll.invoke(selectedTime)
        }

        // pada saat user scroll menit
        val onMinuteScroll : (SimpleModel) -> Unit = { t ->
            selectedTime.minute = t.value
            onScroll.invoke(selectedTime)
        }

        // inisialisasi variabel mode am pm
        // terdapat 2 data kosong dimana
        // ini digunakan sebagai 'gap'
        val modes = arrayListOf(
            SimpleModel(label = "", value = 0, flag = SimpleModel.FLAG_UNSELECTED),
            SimpleModel(label = TimeModel.AM, value = 0, flag = SimpleModel.FLAG_SELECTED),
            SimpleModel(label = TimeModel.PM, value = 0, flag = SimpleModel.FLAG_SELECTED),
            SimpleModel(label = "", value = 0, flag = SimpleModel.FLAG_UNSELECTED)
        )
        this.mode = TimePicker(c, v.findViewById(R.id.recycleview_mode),modes, TimeAdapter.SHOW_MODE, onModeScroll)

        // inisialisasi variabel daftar jam
        // terdapat 2 data kosong dimana
        // ini digunakan sebagai 'gap'
        val hours = ArrayList<SimpleModel>()
        hours.add(SimpleModel(label = "", value = 0, flag = SimpleModel.FLAG_UNSELECTED))
        for (i in rangeHour) hours.add(SimpleModel(label = "${ i }", value = i, flag = SimpleModel.FLAG_SELECTED))
        hours.add(SimpleModel(label = "", value = 0, flag = SimpleModel.FLAG_UNSELECTED))
        this.hour = TimePicker(c, v.findViewById(R.id.recycleview_hour), hours, TimeAdapter.SHOW_HOUR, onHourScroll)

        // inisialisasi variabel daftar menit
        // terdapat 2 data kosong dimana
        // ini digunakan sebagai 'gap'
        val minutes = ArrayList<SimpleModel>()
        minutes.add(SimpleModel(label = "", value = 0, flag = SimpleModel.FLAG_UNSELECTED))
        for (i in 0 until 60) minutes.add(SimpleModel(label = "${ i }", value = i, flag = SimpleModel.FLAG_SELECTED))
        this.minute = TimePicker(c, v.findViewById(R.id.recycleview_minute), minutes, TimeAdapter.SHOW_MINUTE, onMinuteScroll)
        minutes.add(SimpleModel(label = "", value = 0, flag = SimpleModel.FLAG_UNSELECTED))
    }


    // class private untuk
    // per masing masing scroll
    private class TimePicker {
        private var items : ArrayList<SimpleModel>
        private var adapter : TimeAdapter
        private var recycleview : RecyclerView

        // fungsi untuk menentukan daftar yang tidak ikut ter select
        private fun setAllItemNotSelected(items : ArrayList<SimpleModel>){
            for (i in items) i.flag = SimpleModel.FLAG_UNSELECTED
        }

        // kontruktor kelas
        constructor(context: Context, recycleview : RecyclerView, items : ArrayList<SimpleModel>, mode : Int, onScroll : (SimpleModel) ->Unit) {

            // inisialisai varabel item
            // dan adapter yang digunakan
            // oleh recycleview
            this.items = items
            this.adapter = TimeAdapter(context, items, mode)

            // inisialisasi recycleview
            // dan set adapter ke recycleview
            this.recycleview = recycleview
            this.recycleview.adapter = adapter

            // inisialisasi layout
            // yang akan digunakan oleh
            // recycleview
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            this.recycleview.layoutManager = layoutManager

            // jika item lebih dari 0
            // ok, tampilkan semua
            // namun posisi ke 3 yg di
            // set sedang ter select
            if (items.size > 0){
                layoutManager.scrollToPositionWithOffset(1, 0)
                setAllItemNotSelected(items)
                items[2].flag = SimpleModel.FLAG_SELECTED
            }

            // kode untuk mengidentifikasi saat
            // recycleview sedang di scroll
            // oleh user
            this.recycleview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                }

                // saat sedang di scroll
                // set data item yang saat ini
                // berada tepat di tengah2 tampilan
                // view
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