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
import android.util.Log
import android.view.View
import androidx.appcompat.widget.SwitchCompat
import androidx.cardview.widget.CardView
import com.example.minumobat.ui.layout.LayoutDatePicker
import android.view.animation.RotateAnimation

import android.view.animation.DecelerateInterpolator

import android.view.animation.AnimationSet
import android.widget.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.minumobat.model.detail_schedule_model.DetailScheduleModel
import com.example.minumobat.model.schedule_model.ScheduleModel
import com.example.minumobat.model.schedule_model.ScheduleViewModel
import com.example.minumobat.ui.layout.LayoutDetailSchedule
import androidx.lifecycle.ViewModelStoreOwner
import com.example.minumobat.model.detail_schedule_model.DetailScheduleViewModel
import com.example.minumobat.ui.activity.schedule_page.SchedulePageActivity
import com.example.minumobat.ui.dialog.DialogEditDescription
import com.example.minumobat.util.Utils
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule


class HomeActivity : AppCompatActivity() {
    companion object {
        fun createIntent(ctx : Context) : Intent {
            return Intent(ctx, HomeActivity::class.java)
        }
    }
    lateinit var context: Context

    lateinit var darkModeSwitch : SwitchCompat

    lateinit var layoutDatePickerContainer : CardView
    lateinit var textChooseDate : TextView
    lateinit var imageChooseDate : ImageView
    lateinit var linearLayoutPickDate : LinearLayout
    lateinit var layoutDatePicker : LayoutDatePicker

    lateinit var scheduleViewModel : ScheduleViewModel
    lateinit var detailScheduleViewModel : DetailScheduleViewModel

    lateinit var layoutMorningDetailSchedule : LayoutDetailSchedule
    lateinit var layoutAfternoonDetailSchedule : LayoutDetailSchedule
    lateinit var layoutNightDetailSchedule : LayoutDetailSchedule

    lateinit var setAlarmButton : FrameLayout

    var scheduleModel : ScheduleModel? = null

    var morningTime : DetailScheduleModel? = null
    var afternoonTime : DetailScheduleModel? = null
    var nightTime : DetailScheduleModel? = null

    var isSaving = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initWidget()
    }

    private fun initWidget(){
        context = this@HomeActivity

        //checkService(context)

        scheduleViewModel = ViewModelProvider(context as ViewModelStoreOwner).get(ScheduleViewModel::class.java)
        detailScheduleViewModel = ViewModelProvider(context as ViewModelStoreOwner).get(DetailScheduleViewModel::class.java)

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

        setAlarmButton = findViewById(R.id.set_alarm_button)
        setAlarmButton.visibility = View.GONE
        setAlarmButton.setOnClickListener {
            validate()
        }

        layoutDatePicker = LayoutDatePicker(context, findViewById(R.id.layout_date_picker)){ start,end ->
            if (start.isEmpty() or end.isEmpty()){
                return@LayoutDatePicker
            }

            textChooseDate.text = "$start - $end"
            scheduleModel = ScheduleModel()
            scheduleModel!!.startDate = start.parseToDate()
            scheduleModel!!.endDate = end.parseToDate()
        }

        layoutMorningDetailSchedule = LayoutDetailSchedule(context,
            findViewById(R.id.morning_detail_schedule),R.drawable.morning, context.getString(R.string.morning), ArrayList<Int>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)), {
            morningTime = DetailScheduleModel()
            morningTime!!.name = Utils.NAME_MORNING
            morningTime!!.description = layoutMorningDetailSchedule.descriptionText
            morningTime!!.hour = it.hour
            morningTime!!.minute = it.minute
            morningTime!!.mode = it.mode
            morningTime!!.status = DetailScheduleModel.STATUS_ON

        }, {
                displayDescriptionDialogForLayout(it)
                setAlarmButton.visibility = if (scheduleModel != null) View.VISIBLE else View.GONE
        })
        layoutMorningDetailSchedule.layoutID = 1

        layoutAfternoonDetailSchedule = LayoutDetailSchedule(context,
            findViewById(R.id.afternoon_detail_schedule),R.drawable.afternoon, context.getString(R.string.afternoon), ArrayList<Int>(Arrays.asList(10, 11, 12, 1, 2, 3)), {
            afternoonTime = DetailScheduleModel()
            afternoonTime!!.name = Utils.NAME_AFTERNOON
            afternoonTime!!.description = layoutAfternoonDetailSchedule.descriptionText
            afternoonTime!!.hour = it.hour
            afternoonTime!!.minute = it.minute
            afternoonTime!!.mode = it.mode
            afternoonTime!!.status = DetailScheduleModel.STATUS_ON

        }, {
                displayDescriptionDialogForLayout(it)
                setAlarmButton.visibility = if (scheduleModel != null) View.VISIBLE else View.GONE
        })
        layoutAfternoonDetailSchedule.layoutID = 2

        layoutNightDetailSchedule = LayoutDetailSchedule(context,
            findViewById(R.id.night_detail_schedule),R.drawable.night, context.getString(R.string.night), ArrayList<Int>(Arrays.asList(3, 4, 5, 6, 7, 8, 9, 10, 11)), {
            nightTime = DetailScheduleModel()
            nightTime!!.name = Utils.NAME_NIGHT
            nightTime!!.description = layoutNightDetailSchedule.descriptionText
            nightTime!!.hour = it.hour
            nightTime!!.minute = it.minute
            nightTime!!.mode = it.mode
            nightTime!!.status = DetailScheduleModel.STATUS_ON

        }, {
                displayDescriptionDialogForLayout(it)
                setAlarmButton.visibility = if (scheduleModel != null) View.VISIBLE else View.GONE
        })
        layoutNightDetailSchedule.layoutID = 3
    }

    private fun displayDescriptionDialogForLayout(id : Int){
        val dialog = DialogEditDescription(object : (String,String) -> Unit {
            override fun invoke(description: String, phoneNumber: String) {
                when(id){
                    1 -> {
                        layoutMorningDetailSchedule.phoneNumberText = phoneNumber
                        layoutMorningDetailSchedule.setDescription(description)
                    }
                    2 -> {
                        layoutAfternoonDetailSchedule.phoneNumberText = phoneNumber
                        layoutAfternoonDetailSchedule.setDescription(description)
                    }
                    3 -> {
                        layoutMorningDetailSchedule.phoneNumberText = phoneNumber
                        layoutNightDetailSchedule.setDescription(description)
                    }
                }
            }
        })
        dialog.show(supportFragmentManager, "dialog edit description")
    }

    private fun validate(){
        if (scheduleModel == null){
            return
        }

        if (isSaving){
            return
        }

        scheduleViewModel.getAllExistingSchedule(scheduleModel!!.startDate!!, scheduleModel!!.endDate!!, object : MutableLiveData<List<ScheduleModel>>() {
            override fun setValue(value: List<ScheduleModel>) {
                super.setValue(value)
                if ( ! value.isEmpty()){
                    Toast.makeText(context, "Invalid date range!", Toast.LENGTH_SHORT).show()
                    return
                }
                saveSchedule()
            }
        })
    }

    private fun saveSchedule(){
        scheduleViewModel.add(scheduleModel!!, object : MutableLiveData<Long>() {
            override fun setValue(value: Long) {
                super.setValue(value)
                saveDetailSchedule(value)
                Log.e("schedule_save", "id : ${value}")
            }
        })
    }

    fun saveDetailSchedule(scheduleId : Long){
        if (morningTime != null){
            morningTime!!.scheduleID = scheduleId
            detailScheduleViewModel.add(morningTime!!, object : MutableLiveData<Long>() {
                override fun setValue(value: Long) {
                    super.setValue(value)
                    Log.e("morning", "id : ${value}")
                    Log.e("morning", "time : ${morningTime!!.hour}-${morningTime!!.minute}")
                }
            })
        }
        if (afternoonTime != null){
            afternoonTime!!.scheduleID = scheduleId
            detailScheduleViewModel.add(afternoonTime!!, object : MutableLiveData<Long>() {
                override fun setValue(value: Long) {
                    super.setValue(value)
                    Log.e("afternoon", "id : ${value}")
                    Log.e("afternoon", "time : ${afternoonTime!!.hour}-${afternoonTime!!.minute}")
                }
            })
        }
        if (nightTime != null){
            nightTime!!.scheduleID = scheduleId
            detailScheduleViewModel.add(nightTime!!, object : MutableLiveData<Long>() {
                override fun setValue(value: Long) {
                    super.setValue(value)
                    Log.e("night", "id : ${value}")
                    Log.e("night", "time : ${nightTime!!.hour}-${nightTime!!.minute}")
                }
            })
        }

        isSaving = true

        Timer().schedule(1000){
            startActivity(Intent(context, HomeActivity::class.java))
            finish()
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