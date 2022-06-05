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
import com.example.minumobat.util.Utils.Companion.getDatesFromToEnd
import com.example.range_date_picker.adapter.DateAdapter
import java.time.LocalDate

// class untuk layout date picker
// class ini yang akan menghandle proses
// untuk tampilan time picker
@RequiresApi(Build.VERSION_CODES.O)
class LayoutDatePicker {
    companion object {
        val PICKER_TYPE_RANGE = 1
        val PICKER_TYPE_STEP = 2
    }

    private lateinit var ctx: Context

    private lateinit var texview_title : TextView
    private lateinit  var prev : ImageView
    private lateinit  var next : ImageView

    private lateinit var dateAdapter : DateAdapter
    private lateinit var recycleview_date : RecyclerView

    private var currentDate = LocalDate.now()
    private var datePickerModel = DatePickerUtil.setDateToNow(currentDate)

    val selected : ArrayList<DateModel> = ArrayList()
    private var dateResults : ArrayList<DateModel> = ArrayList()

    private var startDate : DateModel = DateModel()
    private var endDate : DateModel = DateModel()

    // kontruktor kelas
    constructor(c: Context, v : View,pickerType : Int, callBack : (ArrayList<DateModel>) -> Unit)  {

        // fungsi inisialisasi dialog
        initDialog(c,v)

        when (pickerType){
            PICKER_TYPE_RANGE -> setAdapterPickerTypeRange(c, datePickerModel, callBack)
            PICKER_TYPE_STEP -> setAdapterPickerTypeStep(c, datePickerModel, callBack)
        }
    }

    private fun initDialog(c: Context, v : View){

        ctx = c

        // inisialisasi tombol
        // next saat ditekan
        // akan ditambahkan 1
        // bulan
        prev = v.findViewById(R.id.prev)
        prev.setOnClickListener {
            currentDate = currentDate.minusMonths(1)
            datePickerModel = DatePickerUtil.setDateToNow(currentDate)
            refreshLayout(c, datePickerModel)
        }

        // inisialisasi tombol
        // next saat ditekan
        // akan kurangi 1
        // bulan
        next = v.findViewById(R.id.next)
        next.setOnClickListener {
            currentDate = currentDate.plusMonths(1)
            datePickerModel = DatePickerUtil.setDateToNow(currentDate)
            refreshLayout(c, datePickerModel)
        }

        // inisialisasi text
        // untuk tampilan nama
        // bulan
        texview_title = v.findViewById(R.id.texview_title)
        recycleview_date = v.findViewById(R.id.recycleview_date)

    }

    fun refresh(){
        refreshLayout(ctx, datePickerModel)
    }

    // fungsi untuk refresh
    // layout saat inisialisasi
    // dialog / bulan di set
    private fun refreshLayout(c: Context, d : DatePickerModel){
        texview_title.text = "${DatePickerUtil.getMonthName(c, d.month - 1)} ${d.year}"
        dateAdapter.setItems(d.days)
        restoreSelection(dateAdapter.list)
        dateAdapter.notifyDataSetChanged()
    }

    // fungsi set adapter
    // untuk menampilkan daftar
    // hari pada satu bulan
    private fun setAdapterPickerTypeRange(c: Context, d : DatePickerModel, callBack : (ArrayList<DateModel>) -> Unit){

        // inisialisasi callback saat hari pilih
        // maka adapter akan memberikan data
        // tanggal brapa yang dipilih
        dateAdapter = DateAdapter(c, d.days){ item, pos ->

            // jika tanggal awal dan akhir sudah dipilih
            // maka tampilan akan di reset ulang
            if ( ! startDate.isEmpty() && !  endDate.isEmpty()){
                startDate = DateModel()
                endDate = DateModel()
                clearSelected(dateAdapter.list)
            }

            // jika tanggal awal dan akhir belum dipilih
            // maka akan di set tanggal awal
            if (startDate.isEmpty() && endDate.isEmpty()){
                startDate = item
                startDate.flag_action = DateModel.FLAG_SELECTED
                dateAdapter.notifyDataSetChanged()
                return@DateAdapter
            }

            // jika tanggal akhir belum dipilih
            // maka akan di set juga
            if (endDate.isEmpty()){
                endDate = item
                endDate.flag_action = DateModel.FLAG_SELECTED
            }

            // jika user memilih tanggal akhir
            // yang mana tanggal tersebut adalah sebelum
            // tanggal awal, maka tampilan akan di reset (error)
            if (startDate.isMoreOrEqualThan(endDate)){
                startDate = DateModel()
                endDate = DateModel()
                clearSelected(dateAdapter.list)
                dateAdapter.notifyDataSetChanged()
                return@DateAdapter
            }

            // jika  kedua tanggal awal dan akhir sudah dipilih
            // tampilkan border bahwa range waktu telah di set
            for (i in dateAdapter.list){
                if (i.isMoreOrEqualThan(startDate) && i.isLessOrEqualThan(endDate)){
                   i.flag_action = DateModel.FLAG_SELECTED
                }
            }

            dateAdapter.notifyDataSetChanged()

            dateResults.clear()
            val dates = getDatesFromToEnd(startDate.parseToDate(), endDate.parseToDate())
            for (date in dates){
                dateResults.add(DateModel.parseFromDate(date))
            }

            // kembalikan data ke tampilan utama
            callBack.invoke(dateResults)
        }

        // set adapter yang digunakan ke
        // recycleview dan juga set layout
        // manager recycleview dengan tampilan grid
        recycleview_date.adapter = dateAdapter
        recycleview_date.apply {
            layoutManager = GridLayoutManager(c, 7)
        }

        // refresh layout
        refreshLayout(c, datePickerModel)
    }

    private fun setAdapterPickerTypeStep(c: Context, d : DatePickerModel, callBack : (ArrayList<DateModel>) -> Unit){
        // inisialisasi callback saat hari pilih
        // maka adapter akan memberikan data
        // tanggal brapa yang dipilih
        dateAdapter = DateAdapter(c, d.days){ item, pos ->

            if (isAlreadyInSelected(item, dateResults)){
                item.flag_action = item.flag
                dateResults.remove(item)

            } else {
                item.flag_action = DateModel.FLAG_SELECTED
                dateResults.add(item)
            }

            dateAdapter.notifyDataSetChanged()

            // kembalikan data ke tampilan utama
            callBack.invoke(dateResults)
        }

        // set adapter yang digunakan ke
        // recycleview dan juga set layout
        // manager recycleview dengan tampilan grid
        recycleview_date.adapter = dateAdapter
        recycleview_date.apply {
            layoutManager = GridLayoutManager(c, 7)
        }

        // refresh layout
        refreshLayout(c, datePickerModel)
    }

    // fungsi untuk menghilangkan
    // tag tanggal yang di pilih oleh user
    private fun clearSelected(days : ArrayList<DateModel>){
        for (item in days) item.flag_action = item.flag
    }

    // fungsi untuk menampilkan kembali
    // tag tanggal yang di pilih oleh user
    // meskipun pindah2 bulan
    private  fun restoreSelection(days : ArrayList<DateModel>){
        if ( ! startDate.isEmpty() &&  ! endDate.isEmpty()){
            for (i in days){
                if (i.isMoreOrEqualThan(startDate) && i.isLessOrEqualThan(endDate)){
                    i.flag_action = DateModel.FLAG_SELECTED
                }
            }
        }
        for (select in selected){
            for (i in days){
                if (i.toString() == select.toString()){
                    i.flag = DateModel.FLAG_NOT_AVALIABLE
                }
            }
        }
    }

    private fun isAlreadyInSelected(d : DateModel, arr : ArrayList<DateModel>) : Boolean {
        for (i in arr){
            if (d.toString() == i.toString()){
                return true
            }
        }
        return false
    }
}