package com.example.minumobat.ui.activity.home

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.minumobat.R
import com.example.minumobat.service.AppReceiver

import android.content.Intent
import com.example.minumobat.service.AppReceiver.Companion.ACTION_RESTART_SERVICE

import com.example.minumobat.service.NotifService
import com.example.minumobat.util.Utils.Companion.isMyServiceRunning

import androidx.appcompat.app.AppCompatDelegate
import android.content.res.Configuration
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.cardview.widget.CardView
import com.example.minumobat.ui.layout.LayoutDatePicker
import android.view.animation.RotateAnimation

import android.view.animation.DecelerateInterpolator

import android.view.animation.AnimationSet
import com.example.minumobat.ui.layout.LayoutDetailSchedule


class HomeActivity : AppCompatActivity() {

    lateinit var context: Context

    lateinit var darkModeSwitch : SwitchCompat

    lateinit var layoutDatePickerContainer : CardView
    lateinit var textChooseDate : TextView
    lateinit var imageChooseDate : ImageView
    lateinit var linearLayoutPickDate : LinearLayout
    lateinit var layoutDatePicker : LayoutDatePicker

    lateinit var layoutMorningDetailSchedule : LayoutDetailSchedule
    lateinit var layoutAfternoonDetailSchedule : LayoutDetailSchedule
    lateinit var layoutNightDetailSchedule : LayoutDetailSchedule

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initWidget()
    }

    private fun initWidget(){
        context = this@HomeActivity

        //checkService(context)

        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        darkModeSwitch = findViewById(R.id.dark_mode_switch)
        darkModeSwitch.isChecked = (currentNightMode == Configuration.UI_MODE_NIGHT_YES)
        darkModeSwitch.setOnCheckedChangeListener {compoundButton, b ->
            AppCompatDelegate.setDefaultNightMode(
                if (b)
                    AppCompatDelegate.MODE_NIGHT_YES
                else
                    AppCompatDelegate.MODE_NIGHT_NO)
        }

        textChooseDate = findViewById(R.id.text_choose_date)
        imageChooseDate = findViewById(R.id.image_choose_date)

        layoutDatePickerContainer = findViewById(R.id.layout_date_picker_container)
        layoutDatePickerContainer.visibility = View.GONE

        linearLayoutPickDate = findViewById(R.id.linearLayout_pick_date)
        linearLayoutPickDate.setOnClickListener {
            val isShow = layoutDatePickerContainer.visibility == View.VISIBLE
            layoutDatePickerContainer.visibility = if (isShow) View.GONE else View.VISIBLE
            rotateDropDownIcon(isShow)
        }

        layoutDatePicker = LayoutDatePicker(this@HomeActivity, findViewById(R.id.layout_date_picker)){ start,end ->
            if (start.isEmpty() or end.isEmpty()){
                return@LayoutDatePicker
            }
            textChooseDate.text = "$start - $end"
        }

        layoutMorningDetailSchedule = LayoutDetailSchedule(this@HomeActivity, findViewById(R.id.morning_detail_schedule),R.drawable.morning, "Morning", 0, 10){

        }
        layoutAfternoonDetailSchedule = LayoutDetailSchedule(this@HomeActivity, findViewById(R.id.afternoon_detail_schedule),R.drawable.afternoon, "Afternoon", 3, 10){

        }
        layoutNightDetailSchedule = LayoutDetailSchedule(this@HomeActivity, findViewById(R.id.night_detail_schedule),R.drawable.night, "Night", 3, 11){

        }
    }


    private fun rotateDropDownIcon(isShow : Boolean){
        val animSet = AnimationSet(true)
        animSet.interpolator = DecelerateInterpolator()
        animSet.fillAfter = true
        animSet.isFillEnabled = true

        val animRotate = RotateAnimation(
            if (isShow) -180.0f else 0f,
            if (isShow) 0f else -180.0f,
            RotateAnimation.RELATIVE_TO_SELF, 0.5f,
            RotateAnimation.RELATIVE_TO_SELF, 0.5f
        )

        animRotate.duration = 350
        animRotate.fillAfter = true
        animSet.addAnimation(animRotate)
        imageChooseDate.startAnimation(animSet)
    }

    private fun checkService(context : Context){
        if (!isMyServiceRunning(context, NotifService::class.java)) {
            val i = Intent(ACTION_RESTART_SERVICE)
            i.setClass(baseContext, AppReceiver::class.java)
            baseContext.sendBroadcast(i)
        }
    }
}