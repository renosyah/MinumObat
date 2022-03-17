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

    var descriptionText : String = ""
    var phoneNumberText : String = ""

    constructor(c: Context, v : View, image : Int, text : String, rangeHour : ArrayList<Int>, onScroll : (TimeModel) -> Unit, onDescriptionClick : (Int) -> Unit)  {

        this.imageTime = v.findViewById(R.id.image_time)
        this.imageTime.setImageResource(image)

        this.textTime = v.findViewById(R.id.text_time)
        this.textTime.text = text

        this.openTimePickerButton = v.findViewById(R.id.open_time_picker_button)
        this.openTimePickerButton.visibility = View.VISIBLE
        this.openTimePickerButton.setOnClickListener {
            toggleLayoutTimePicker()
        }

        this.layoutDetailSchedule = v.findViewById(R.id.layout_detail_schedule)
        this.layoutDetailSchedule.setOnClickListener {
            toggleLayoutTimePicker()
        }

        this.timeInputDisplayLayout = v.findViewById(R.id.time_input_display_layout)
        this.timeInputDisplayLayout.visibility = View.GONE

        this.timeDisplayTextView = v.findViewById(R.id.time_display_text)
        this.amPmDisplayText = v.findViewById(R.id.am_pm_display)

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

    fun setDescription(text : String){
        this.descriptionText = text
        this.layoutTimePicker.descriptionButton.text = this.descriptionText
        this.textTime.text = this.descriptionText
    }

    private fun toggleLayoutTimePicker(){
        this.layoutTimePickerView.visibility = if (this.layoutTimePickerView.visibility == View.GONE) View.VISIBLE else View.GONE
    }
}