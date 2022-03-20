package com.example.minumobat.ui.adapter

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.minumobat.R
import com.example.minumobat.model.detail_schedule_model.DetailScheduleModel
import com.example.minumobat.model.time_picker_model.TimeModel
import com.example.minumobat.util.Utils
import java.util.*
import kotlin.collections.ArrayList

// kelas adapter yang digunakan untuk
// menhadle repetitif task untuk
// menampilkan data dengan recycleview
class DetailScheduleAdapter : RecyclerView.Adapter<DetailScheduleAdapter.Holder>  {
    var context: Context
    var list : ArrayList<DetailScheduleModel> = ArrayList()
    var onClick : (DetailScheduleModel, Int) -> Unit
    var onSwitch : (DetailScheduleModel, Int) -> Unit

    // kelas konstruktor
    constructor(context: Context, list : ArrayList<DetailScheduleModel>, onClick : (DetailScheduleModel, Int) -> Unit, onSwitch : (DetailScheduleModel, Int) -> Unit) : super() {
        this.context = context
        this.list = list
        this.onClick = onClick
        this.onSwitch = onSwitch
    }

    // fungsi yang dipanggil
    // saat layout akan ditampilkan
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder((context as Activity).layoutInflater.inflate(R.layout.detail_shcedule_adapter, parent, false))
    }

    // fungsi untuk menampilkan data ke tampilan
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = list.get(position)
        val current = TimeModel.fromTime(item.time)

        // set warna latar belakang
        // dengan kondisi apakah
        // tanggal berada dalam range waktu
        holder.layout.setBackgroundResource(
            if (Utils.isBetween(item.name, current)) R.drawable.detail_shcedule_adapter_border_shape_selected
            else R.drawable.detail_shcedule_adapter_border_shape)

        // tampilkan data text
        // nama dan waktu
        holder.name.text = item.name
        holder.time.text = current.toString()
        holder.mode.text = current.mode
        holder.description.text = item.description
        holder.switch.isChecked = (item.status == DetailScheduleModel.STATUS_ON)

        // set callback untuk tombol switch
        // saat tombol switch di slide oleh user
        // maka warnanya akan diubah sesuai flag
        // dan mengembalika value set nya
        holder.switch.setOnCheckedChangeListener {compoundButton, b ->
            item.status = if (b) DetailScheduleModel.STATUS_ON else DetailScheduleModel.STATUS_OFF
            checkSwitch(holder, item, current)
            onSwitch.invoke(item, position)
        }

        // set callback untuk layout
        // yang mana saat di klik akan
        // mengembalikan value
        holder.layout.setOnClickListener {
            onClick.invoke(item,position)
        }

        // memanggil fungsi untuk check warna switch
        // berdasarkan flag di data item
        checkSwitch(holder, item, current)
    }

    // fungsi untuk menentukan
    // warna latar switch berdasarkan
    // flag yang diset
    private fun checkSwitch(holder: Holder, item : DetailScheduleModel, current : TimeModel){
        val check = (item.status == DetailScheduleModel.STATUS_ON)

        var thumbDrawable = ContextCompat.getDrawable(context,R.drawable.switch_rounded_thumb_disable)
        var trackDrawable = ContextCompat.getDrawable(context, R.drawable.switch_rounded_track_disable)

        if (Utils.isBetween(item.name, current)){
            if (check) {
                thumbDrawable = ContextCompat.getDrawable(context,R.drawable.switch_rounded_thumb_enable_selected)
                trackDrawable = ContextCompat.getDrawable(context, R.drawable.switch_rounded_track_enable_selected)
            }

        } else {
            if (check) {
                thumbDrawable = ContextCompat.getDrawable(context,R.drawable.switch_rounded_thumb_enable)
                trackDrawable = ContextCompat.getDrawable(context, R.drawable.switch_rounded_track_enable)
            }

        }

        holder.switch.thumbDrawable = thumbDrawable
        holder.switch.trackDrawable = trackDrawable
    }

    // fungsi untuk menentukan
    // jumlah layout yang akan ditampilkan
    // biasanya diambil dari jumlah
    // data list
    override fun getItemCount(): Int {
        return list.size
    }

    // kelas holder yang digunakan
    // untuk deklarasi dan inisialisasi
    // widget layout
    class Holder : RecyclerView.ViewHolder {
        lateinit var layout : LinearLayout
        lateinit var switch : SwitchCompat
        lateinit var name : TextView
        lateinit var time : TextView
        lateinit var mode : TextView
        lateinit var description : TextView

        constructor(v: View) : super(v) {
            layout = v.findViewById(R.id.adapter_layout)
            switch = v.findViewById(R.id.detail_schedule_switch)
            name = v.findViewById(R.id.name_detail_schedule)
            time = v.findViewById(R.id.time_detail_schedule)
            mode = v.findViewById(R.id.time_detail_schedule_mode)
            description= v.findViewById(R.id.description_detail_schedule)
        }
    }
}