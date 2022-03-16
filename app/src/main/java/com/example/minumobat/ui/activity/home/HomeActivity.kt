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


class HomeActivity : AppCompatActivity() {

    lateinit var context: Context

    lateinit var darkModeSwitch : SwitchCompat

    lateinit var linearLayoutPickDate : LinearLayout
    lateinit var layoutDatePickerContainer : CardView
    lateinit var textChooseDate : TextView
    lateinit var imageChooseDate : ImageView
    lateinit var layoutDatePicker : LayoutDatePicker

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
            imageChooseDate.rotation = if (isShow) 0.toFloat() else 180.toFloat()
        }

        layoutDatePicker = LayoutDatePicker(this@HomeActivity, findViewById(R.id.layout_date_picker)){ item ->
            textChooseDate.text = item.toString()
        }
    }

    private fun checkService(context : Context){
        if (!isMyServiceRunning(context, NotifService::class.java)) {
            val i = Intent(ACTION_RESTART_SERVICE)
            i.setClass(baseContext, AppReceiver::class.java)
            baseContext.sendBroadcast(i)
        }
    }
}