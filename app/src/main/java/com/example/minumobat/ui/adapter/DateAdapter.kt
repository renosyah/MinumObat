package com.example.range_date_picker.adapter

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.minumobat.R
import com.example.minumobat.model.date_picker_model.DateModel

// kelas adapter yang digunakan untuk
// menhadle repetitif task untuk
// menampilkan data dengan recycleview
class DateAdapter : RecyclerView.Adapter<DateAdapter.Holder> {
    var context: Context
    var list : ArrayList<DateModel> = ArrayList()
    var onClick : (DateModel, Int) -> Unit

    // kelas konstruktor
    constructor(context: Context, list : ArrayList<DateModel>, onClick : (DateModel, Int) -> Unit) : super() {
        this.context = context
        this.list = list
        this.onClick = onClick
    }

    // fungsi untuk mengeset daftar list
    // item yang digunakan untuk iterasi adapter
    fun setItems(list : ArrayList<DateModel>){
        this.list = list
        this.notifyDataSetChanged()
    }

    // fungsi yang dipanggil
    // saat layout akan ditampilkan
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder((context as Activity).layoutInflater.inflate(R.layout.date_adapter,parent,false))
    }

    // fungsi untuk menampilkan data ke tampilan
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = list[position]

        // tamnpikan text tanggal hari
        // dan setting warna latar belakang
        // berdasarkan flag status
        holder.textview_day.text =  "${ item.day }"
        setBackgroundColor(holder, if (item.flag_action == DateModel.FLAG_NONE) item.flag else item.flag_action)

        // inisialisasi fungsi callback
        // dan jika hari tidak tersedia
        // hentikan proses program
        holder.layout_background.setOnClickListener {
            onClick.invoke(item, position)
        }
    }

    // fungsi untuk menentukan
    // warna latar belakang berdasarkan
    // flag yang diset
    private fun setBackgroundColor(holder: Holder, flag : Int){
        when (flag){
            DateModel.FLAG_SELECTED -> {
                holder.textview_day.setTextColor(ContextCompat.getColor(context, R.color.textColorSelected))
                holder.layout_panel.setCardBackgroundColor(ContextCompat.getColor(context, R.color.layoutPanelSelected))
                holder.layout_background.setCardBackgroundColor(ContextCompat.getColor(context, R.color.layoutBackgroundSelected))
            }
            DateModel.FLAG_CURRENT -> {
                holder.textview_day.setTextColor(ContextCompat.getColor(context, R.color.textColorCurrent))
                holder.layout_panel.setCardBackgroundColor(ContextCompat.getColor(context, R.color.layoutPanelCurrent))
                holder.layout_background.setCardBackgroundColor(ContextCompat.getColor(context, R.color.layoutBackgroundCurrent))
            }
            DateModel.FLAG_UNSELECTED -> {
                holder.textview_day.setTextColor(ContextCompat.getColor(context, R.color.textColorDefault))
                holder.layout_panel.setCardBackgroundColor(ContextCompat.getColor(context, R.color.layoutPanelDefault))
                holder.layout_background.setCardBackgroundColor(ContextCompat.getColor(context, R.color.layoutBackgroundDefault))
            }
            DateModel.FLAG_UNREACH -> {
                holder.textview_day.setTextColor(ContextCompat.getColor(context, R.color.textColorUnreach))
                holder.layout_panel.setCardBackgroundColor(ContextCompat.getColor(context, R.color.layoutPanelUreach))
                holder.layout_background.setCardBackgroundColor(ContextCompat.getColor(context, R.color.layoutBackgroundUnreach))
            }
            DateModel.FLAG_NOT_AVALIABLE-> {
                holder.textview_day.setTextColor(ContextCompat.getColor(context, R.color.green_button))
                holder.layout_panel.setCardBackgroundColor(ContextCompat.getColor(context, R.color.layoutPanelUreach))
                holder.layout_background.setCardBackgroundColor(ContextCompat.getColor(context, R.color.layoutBackgroundUnreach))
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
        var layout_background : CardView
        var layout_panel : CardView
        var textview_day : TextView

        constructor(itemView: View) : super(itemView) {
            this.layout_background = itemView.findViewById(R.id.layout_background)
            this.layout_panel = itemView.findViewById(R.id.layout_panel)
            this.textview_day = itemView.findViewById(R.id.textview_day)
        }
    }
}