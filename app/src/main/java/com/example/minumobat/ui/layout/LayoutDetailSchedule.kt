package com.example.minumobat.ui.layout

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.minumobat.R
import com.example.minumobat.model.time_picker_model.TimeModel

// kelas untuk detail schedule
// dimana kelass ini menghandle
// tampilan detail schedule
// dimenu home
class LayoutDetailSchedule {
    var layoutID = 0
    lateinit var layoutDetailSchedule : CardView

    lateinit var imageTime : ImageView
    lateinit var textTime : TextView

    lateinit var openTimePickerButton : FrameLayout
    lateinit var timeInputDisplayLayout : LinearLayout

    lateinit var timeDisplayTextView: TextView
    lateinit var amPmDisplayText : TextView

    lateinit var layoutTimePickerView : View
    lateinit var layoutTimePicker : LayoutTimePicker

    // konstruktor kelas
    constructor(c: Context, id : Int, v : View, image : Int, text : String, rangeHour : ArrayList<Int>, onScroll : (TimeModel) -> Unit, onDescriptionClick : (Int) -> Unit)  {
        this.layoutID = id

        // inisialisasi gambar icon
        this.imageTime = v.findViewById(R.id.image_time)
        this.imageTime.setImageResource(image)

        // inisialisi text nama label
        this.textTime = v.findViewById(R.id.text_time)
        this.textTime.text = text

        // inisialisasi layout
        // yang digunakan sebagai interaksi
        // untuk tombol
        this.openTimePickerButton = v.findViewById(R.id.open_time_picker_button)
        this.openTimePickerButton.visibility = View.VISIBLE
        this.openTimePickerButton.setOnClickListener {
            toggleLayoutTimePicker()
        }

        // inisialisasi layout
        // yang juga digunakan sebagai interaksi
        // untuk tombol
        this.layoutDetailSchedule = v.findViewById(R.id.layout_detail_schedule)
        this.layoutDetailSchedule.setOnClickListener {
            toggleLayoutTimePicker()
        }

        // inisialisasi text
        // yang digunakan sebagai
        // tampilan waktu yang dipilih user
        this.timeInputDisplayLayout = v.findViewById(R.id.time_input_display_layout)
        this.timeInputDisplayLayout.visibility = View.GONE

        // inisialisasi text
        // yang digunakan sebagai
        // tampilan mode yang dipilih user
        this.timeDisplayTextView = v.findViewById(R.id.time_display_text)
        this.amPmDisplayText = v.findViewById(R.id.am_pm_display)

        // inisialisasi layout
        // time picker sekaligus
        // callback saat time picker
        // telah di set / tombol
        // deskripsi di tekan
        this.layoutTimePickerView = v.findViewById(R.id.layout_time_picker)
        this.layoutTimePicker = LayoutTimePicker(c, layoutTimePickerView, rangeHour,{
            timeInputDisplayLayout.visibility = View.VISIBLE
            openTimePickerButton.visibility = View.GONE
            timeDisplayTextView.text = it.toString()
            amPmDisplayText.text = it.mode
            onScroll.invoke(it.duplicate())
        },{
            onDescriptionClick.invoke(layoutID)
        })
    }

    // fungsi untuk mengeset
    // dan menampilkan text deskripsi
    fun setDescription(text : String){
        this.layoutTimePicker.descriptionButton.text = text
        this.textTime.text = text
    }

    // fungsi untuk menampilkan
    // sekaligus menyembunyikan
    // tampilan time picker
    private fun toggleLayoutTimePicker(){
        this.layoutTimePickerView.visibility = if (this.layoutTimePickerView.visibility == View.GONE) View.VISIBLE else View.GONE
    }
}