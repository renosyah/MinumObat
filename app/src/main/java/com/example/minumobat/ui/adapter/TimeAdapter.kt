package com.example.range_date_picker.adapter

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.minumobat.R
import com.example.minumobat.model.SimpleModel
import java.text.DecimalFormat
import java.text.NumberFormat

// kelas adapter yang digunakan untuk
// menhadle repetitif task untuk
// menampilkan data dengan recycleview
class TimeAdapter : RecyclerView.Adapter<TimeAdapter.Holder> {
    var context: Context
    var list : ArrayList<SimpleModel>? = null
    var show_what = SHOW_HOUR

    // kelas konstruktor
    constructor(context: Context, list : ArrayList<SimpleModel>, show_what: Int) : super() {
        this.context = context
        this.list = list
        this.show_what = show_what
    }

    // fungsi untuk mengeset daftar list
    // item yang digunakan untuk iterasi adapter
    fun setItems(list : ArrayList<SimpleModel>){
        this.list = list
        this.notifyDataSetChanged()
    }

    // fungsi yang dipanggil
    // saat layout akan ditampilkan
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder((context as Activity).layoutInflater.inflate(R.layout.time_adapter, parent, false))
    }

    // fungsi untuk menampilkan data ke tampilan
    override fun onBindViewHolder(holder: Holder, position: Int) {
        if (list == null) return
        val item = list!!.get(position)
        val f : NumberFormat = DecimalFormat("00")

        when (show_what){
            SHOW_MODE -> holder.textview_time.text =  "${ item.label }"
            else -> holder.textview_time.text = if (item.value != 0 || item.label != "") "${ f.format(item.value) }" else ""
        }
        setBackgroundColor(holder, item.flag)
    }

    private fun setBackgroundColor(holder: Holder, flag : Int){
        when (flag){
            SimpleModel.FLAG_SELECTED -> {
                holder.textview_time.setTextColor(ContextCompat.getColor(context, R.color.timeTextColorSelected))
            }
            SimpleModel.FLAG_UNSELECTED -> {
                holder.textview_time.setTextColor(ContextCompat.getColor(context, R.color.timeTextColorUnreach))
            }
        }
    }

    // fungsi untuk menentukan
    // jumlah layout yang akan ditampilkan
    // biasanya diambil dari jumlah
    // data list
    override fun getItemCount(): Int {
        if (list == null) return 0
        return list!!.size
    }

    // kelas holder yang digunakan
    // untuk deklarasi dan inisialisasi
    // widget layout
    class Holder : RecyclerView.ViewHolder {
        var textview_time : TextView

        constructor(itemView: View) : super(itemView) {
            this.textview_time = itemView.findViewById(R.id.textview_time)
        }
    }

    companion object {
        val SHOW_HOUR = 0
        val SHOW_MINUTE = 1
        val SHOW_SECOND = 2
        val SHOW_MODE = 3
    }
}