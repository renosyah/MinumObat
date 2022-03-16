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

    var selectedTime = TimeModel()

    constructor(c: Context, v : View, image : Int, text : String, startHour : Int, endHour : Int, onScroll : (TimeModel) -> Unit, onDescriptionClick : (Int) -> Unit)  {

        imageTime = v.findViewById(R.id.image_time)
        imageTime.setImageResource(image)

        textTime = v.findViewById(R.id.text_time)
        textTime.text = text

        openTimePickerButton = v.findViewById(R.id.open_time_picker_button)
        openTimePickerButton.visibility = View.VISIBLE
        openTimePickerButton.setOnClickListener {
            toggleLayoutTimePicker()
            onScroll.invoke(selectedTime)
        }

        layoutDetailSchedule = v.findViewById(R.id.layout_detail_schedule)
        layoutDetailSchedule.setOnClickListener {
            toggleLayoutTimePicker()
            onScroll.invoke(selectedTime)
        }

        timeInputDisplayLayout = v.findViewById(R.id.time_input_display_layout)
        timeInputDisplayLayout.visibility = View.GONE

        timeDisplayTextView = v.findViewById(R.id.time_display_text)
        amPmDisplayText = v.findViewById(R.id.am_pm_display)

        layoutTimePickerView = v.findViewById(R.id.layout_time_picker)
        layoutTimePicker = LayoutTimePicker(c, layoutTimePickerView, startHour, endHour,{
            timeInputDisplayLayout.visibility = View.VISIBLE
            openTimePickerButton.visibility = View.GONE
            timeDisplayTextView.text = it.toString()
            amPmDisplayText.text = it.mode
            selectedTime = it.duplicate()
        },{
            onDescriptionClick.invoke(layoutID)
        })
    }

    fun setDescription(text : String){
        layoutTimePicker.description.text = text
        textTime.text = text
    }

    private fun toggleLayoutTimePicker(){
        layoutTimePickerView.visibility = if (layoutTimePickerView.visibility == View.GONE) View.VISIBLE else View.GONE
    }
}