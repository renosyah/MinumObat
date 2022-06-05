package com.example.minumobat.ui.activity.home_inject

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationSet
import android.view.animation.DecelerateInterpolator
import android.view.animation.RotateAnimation
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.cardview.widget.CardView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.minumobat.R
import com.example.minumobat.model.date_picker_model.DateModel
import com.example.minumobat.model.schedule_model.ScheduleModel
import com.example.minumobat.model.schedule_model.ScheduleViewModel
import com.example.minumobat.model.time_picker_model.TimeModel
import com.example.minumobat.service.AppReceiver
import com.example.minumobat.service.NotifService
import com.example.minumobat.ui.activity.home.HomeActivity
import com.example.minumobat.ui.dialog.DialogEditDescription
import com.example.minumobat.ui.layout.LayoutDatePicker
import com.example.minumobat.ui.layout.LayoutDetailSchedule
import com.example.minumobat.util.Utils
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule

class HomeInjectActivity : AppCompatActivity() {
    companion object {

        // fungsi static untuk intent
        // jika dari activity lain ingin
        // menjalankan activity ini
        fun createIntent(ctx : Context) : Intent {
            return Intent(ctx, HomeInjectActivity::class.java)
        }
    }


    // deklarasi variabel
    // yang digunakan dalam
    // activity ini
    lateinit var context: Context
    lateinit var darkModeSwitch : SwitchCompat
    lateinit var layoutDatePickerContainer : CardView
    lateinit var textChooseDate : TextView
    lateinit var imageChooseDate : ImageView
    lateinit var linearLayoutPickDate : LinearLayout
    lateinit var layoutDatePicker : LayoutDatePicker
    lateinit var scheduleViewModel : ScheduleViewModel

    lateinit var setAlarmButton : FrameLayout

    val dates : ArrayList<DateModel> = ArrayList()

    val morningTimes : ArrayList<ScheduleModel> = ArrayList()
    var isSaving = false

    // fungsi yang akan dijalankan pertama kali saat
    // activity di buat dan diproses serta ditampilkan
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_inject)
        initWidget()
    }
    // fungsi yang akan dijalankan
    // untuk inisialisasi variabel
    // dan memanggil fungsi awal
    private fun initWidget() {
        context = this@HomeInjectActivity

        // mengecheck service
        checkService(context)

        // inisialisasi instance
        // yang akan digunakan untuk query
        // ke database
        scheduleViewModel = ViewModelProvider(context as ViewModelStoreOwner).get(ScheduleViewModel::class.java)

        // check apakah perangkat
        // user sedang menggunakan
        // mode night mode / dark mode
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        // inisialisasi witch untuk
        // dark mode / night mode switch
        darkModeSwitch = findViewById(R.id.dark_mode_switch)
        darkModeSwitch.isChecked = (currentNightMode == Configuration.UI_MODE_NIGHT_YES)
        darkModeSwitch.setOnCheckedChangeListener {compoundButton, b ->
            AppCompatDelegate.setDefaultNightMode(if (b) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
        }

        // inisialisasi form
        // untuk date picker
        textChooseDate = findViewById(R.id.text_choose_date)
        imageChooseDate = findViewById(R.id.image_choose_date)

        // inisialisasi layout
        // untuk menampung layout date picker
        layoutDatePickerContainer = findViewById(R.id.layout_date_picker_container)
        layoutDatePickerContainer.visibility = View.GONE

        // inisialisasi layout
        // yang mana saat di tekan
        // akan menampilkan layout
        // date picker
        linearLayoutPickDate = findViewById(R.id.linearLayout_pick_date)
        linearLayoutPickDate.setOnClickListener {
            val isShow = layoutDatePickerContainer.visibility == View.VISIBLE
            layoutDatePickerContainer.visibility = if (isShow) View.GONE else View.VISIBLE
            rotateDropDownIcon(isShow)
        }

        // inisialisasi tombol
        // untuk save, saat diklik akan
        // memanggil fungsi validasi
        setAlarmButton = findViewById(R.id.set_alarm_button)
        setAlarmButton.visibility = View.GONE
        setAlarmButton.setOnClickListener {
            displayDescriptionDialogForLayout()
        }

        // inisialisasi callback untuk date picker
        // saat user memilih tanggal makan akan ditampilkan
        // dan di set ke variabel shcedule model
        layoutDatePicker = LayoutDatePicker(context, findViewById(R.id.layout_date_picker), LayoutDatePicker.PICKER_TYPE_STEP){ results ->
            if (results.isEmpty()){
                return@LayoutDatePicker
            }
            textChooseDate.text = "${results[0]} - ${results[results.size - 1]}"

            dates.clear()
            dates.addAll(results)

            setAlarmButton.visibility = if (dates.isNotEmpty()) View.VISIBLE else View.GONE
        }
        query()
    }

    // fungsi query untuk
    // melakukan query ke database
    // dan mengambil data schedule
    // berdasarkan tanggal wajtu saat ini
    private fun query(){
        scheduleViewModel.getAll(object : MutableLiveData<List<ScheduleModel>>() {
            override fun setValue(value: List<ScheduleModel>) {
                super.setValue(value)
                layoutDatePicker.selected.clear()
                for (i in value){
                    layoutDatePicker.selected.add(DateModel.parseFromDate(i.schedule_date!!))
                }
                layoutDatePicker.refresh()
            }
        })
    }

    // fungsi untuk menampikan dialog
    // untuk user menginputkan deskripsi
    // nomor telp serta nama dokter
    private fun displayDescriptionDialogForLayout(){
        val dialog = DialogEditDescription(object : (String,String,String) -> Unit {
            override fun invoke(description: String,doctorName: String, phoneNumber: String) {
                morningTimes.clear()

                for (i in 0..2){
                    val morningTime = ScheduleModel()
                    morningTime.name = Utils.NAME_MORNING
                    morningTime.doctorName = doctorName
                    morningTime.description = description
                    morningTime.emergencyNumber = phoneNumber
                    morningTime.status = ScheduleModel.STATUS_ON

                    morningTimes.add(morningTime)
                }

                morningTimes[0].time = TimeModel(9, 30, 0, TimeModel.AM).parseToTime()
                morningTimes[1].time = TimeModel(9, 45, 0, TimeModel.AM).parseToTime()
                morningTimes[2].time = TimeModel(10, 0, 0, TimeModel.AM).parseToTime()

                validate()
            }
        })
        dialog.show(supportFragmentManager, "dialog edit description")
    }

    // fungsi untuk melakukan validasi
    // tanggal untuk memastikan
    // tanggal yang dipilih tidak intersect
    // dengan data yang telah ada di database
    private fun validate(){
        if (dates.isEmpty()) return
        if (isSaving) return
        saveSchedule()
    }

    // fungsi untuk menyimpan data detail schedule
    // yang mana terdapat data siang, malam dan pagi
    private fun saveSchedule(){
        isSaving = true

        val schedules = ArrayList<ScheduleModel>()

        for (morningTime in morningTimes){
            for (date in dates){
                val data = ScheduleModel()

                data.name = morningTime.name
                data.description = morningTime.description
                data.time = morningTime.time
                data.doctorName = morningTime.doctorName
                data.emergencyNumber = morningTime.emergencyNumber

                data.schedule_date = date.parseToDate()
                data.typeMedicine = ScheduleModel.TYPE_INJECTION_MEDICINE
                data.status = ScheduleModel.STATUS_ON
                schedules.add(data)
            }
        }

        for (scheduleModel in schedules){
            Log.e("schedule_save", "time : ${scheduleModel.time}, date : ${scheduleModel.schedule_date}")

            scheduleViewModel.add(scheduleModel, object : MutableLiveData<Long>() {
                override fun setValue(value: Long) {
                    super.setValue(value)
                    Log.e("schedule_save", "id : ${value}")
                }
            })
        }

        Timer().schedule(500){
            startActivity(Intent(context, HomeInjectActivity::class.java))
            finish()
        }
    }

    // fungsi untuk memutar gambar
    // arah panah saat date picker
    // dipilih
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

    // fungsi untuk mengecheck
    // dan mengaktifkan service
    // notifikasi
    private fun checkService(context : Context){
        if (!Utils.isMyServiceRunning(context, NotifService::class.java)) {
            val i = Intent(AppReceiver.ACTION_RESTART_SERVICE)
            i.setClass(baseContext, AppReceiver::class.java)
            baseContext.sendBroadcast(i)
        }
    }
}