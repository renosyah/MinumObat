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
import android.view.animation.RotateAnimation
import android.view.animation.DecelerateInterpolator
import android.view.animation.AnimationSet
import android.widget.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.minumobat.model.schedule_model.ScheduleModel
import com.example.minumobat.model.schedule_model.ScheduleViewModel
import androidx.lifecycle.ViewModelStoreOwner
import com.example.minumobat.model.date_picker_model.DateModel
import com.example.minumobat.model.time_picker_model.TimeModel
import com.example.minumobat.ui.dialog.DialogEditDescription
import com.example.minumobat.ui.layout.*
import com.example.minumobat.util.Utils
import java.sql.Date
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule


class HomeActivity : AppCompatActivity() {
    companion object {

        // fungsi static untuk intent
        // jika dari activity lain ingin
        // menjalankan activity ini
        fun createIntent(ctx : Context) : Intent {
            return Intent(ctx, HomeActivity::class.java)
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
    lateinit var layoutMorningDetailSchedule : JamPagi
    lateinit var layoutAfternoonDetailSchedule : JamSiang
    lateinit var layoutNightDetailSchedule : JamMalam
    lateinit var setAlarmButton : FrameLayout

    val dates : ArrayList<DateModel> = ArrayList()

    val morningTime : ScheduleModel = ScheduleModel()
    val afternoonTime : ScheduleModel = ScheduleModel()
    val nightTime : ScheduleModel = ScheduleModel()

    var isSaving = false

    // fungsi yang akan dijalankan pertama kali saat
    // activity di buat dan diproses serta ditampilkan
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initWidget()
    }

    // fungsi yang akan dijalankan
    // untuk inisialisasi variabel
    // dan memanggil fungsi awal
    private fun initWidget(){
        context = this@HomeActivity

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
            validate()
        }

        // inisialisasi callback untuk date picker
        // saat user memilih tanggal makan akan ditampilkan
        // dan di set ke variabel shcedule model
        layoutDatePicker = LayoutDatePicker(context, findViewById(R.id.layout_date_picker), object : DatePickerBase.RangeDatePickerCallback {
            override fun onDateResults(results: ArrayList<DateModel>) {
                if (results.isEmpty()){
                    return
                }
                textChooseDate.text = "${results[0]} - ${results[results.size - 1]}"

                dates.clear()
                dates.addAll(results)

                layoutMorningDetailSchedule.enable = true
                layoutAfternoonDetailSchedule.enable = true
                layoutNightDetailSchedule.enable = true
            }
        })

        // inisialisasi callback untuk detail schedule
        // saat user memilih time makan akan ditampilkan
        // dan di set ke variabel detal shcedule model
        // dimana layout ini khusus untuk menangani schedule pagi
        layoutMorningDetailSchedule = JamPagi(context, 1,
            findViewById(R.id.morning_detail_schedule),R.drawable.morning, context.getString(R.string.morning), ArrayList<Int>(listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)), {
                morningTime.name = Utils.NAME_MORNING
                morningTime.time = TimeModel(it.hour, it.minute, 0, it.mode).parseToTime()

        }, { displayDescriptionDialogForLayout(it) })

        // inisialisasi callback untuk detail schedule
        // saat user memilih time makan akan ditampilkan
        // dan di set ke variabel detal shcedule model
        // dimana layout ini khusus untuk menangani schedule siang
        layoutAfternoonDetailSchedule = JamSiang(context,2,
            findViewById(R.id.afternoon_detail_schedule),R.drawable.afternoon, context.getString(R.string.afternoon), ArrayList<Int>(listOf(10, 11, 12, 1, 2, 3)), {
                afternoonTime.name = Utils.NAME_AFTERNOON
                afternoonTime.time = TimeModel(it.hour, it.minute, 0, it.mode).parseToTime()

        }, { displayDescriptionDialogForLayout(it) })

        // inisialisasi callback untuk detail schedule
        // saat user memilih time makan akan ditampilkan
        // dan di set ke variabel detal shcedule model
        // dimana layout ini khusus untuk menangani schedule malam
        layoutNightDetailSchedule = JamMalam(context, 3,
            findViewById(R.id.night_detail_schedule),R.drawable.night, context.getString(R.string.night), ArrayList<Int>(listOf(3, 4, 5, 6, 7, 8, 9, 10, 11)), {
                nightTime.name = Utils.NAME_NIGHT
                nightTime.time = TimeModel(it.hour, it.minute, 0, it.mode).parseToTime()

        }, { displayDescriptionDialogForLayout(it) })

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
    private fun displayDescriptionDialogForLayout(id : Int){
        val dialog = DialogEditDescription(object : (String,String,String) -> Unit {
            override fun invoke(description: String,doctorName: String, phoneNumber: String) {
                when(id){
                    1 -> {
                        layoutMorningDetailSchedule.setDescription(description)
                        morningTime.doctorName = doctorName
                        morningTime.description = description
                        morningTime.emergencyNumber = phoneNumber
                        morningTime.status = ScheduleModel.STATUS_ON
                    }
                    2 -> {
                        layoutAfternoonDetailSchedule.setDescription(description)
                        afternoonTime.doctorName = doctorName
                        afternoonTime.description = description
                        afternoonTime.emergencyNumber = phoneNumber
                        afternoonTime.status = ScheduleModel.STATUS_ON
                    }
                    3 -> {
                        layoutNightDetailSchedule.setDescription(description)
                        nightTime.doctorName = doctorName
                        nightTime.description = description
                        nightTime.emergencyNumber = phoneNumber
                        nightTime.status = ScheduleModel.STATUS_ON
                    }
                }
                displaySaveButton()
            }
        })
        dialog.show(supportFragmentManager, "dialog edit description")
    }

    // fungsi yang digunakan
    // untuk menampilkan tombol save
    // apabila kondisi form telah sepenuhnya
    // semua diisi dengan benar oleh user
    private fun displaySaveButton(){
        Log.e("valid", "${(dates.isEmpty())} ${! morningTime.isValid()} ${ ! afternoonTime.isValid()} ${ ! nightTime.isValid()}")
        if (dates.isEmpty()) return
        if ( ! morningTime.isValid()) return
        if ( ! afternoonTime.isValid()) return
        if ( ! nightTime.isValid()) return
        setAlarmButton.visibility = View.VISIBLE
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
        for (date in dates){
            val data = ScheduleModel()

            data.name = morningTime.name
            data.description = morningTime.description
            data.time = morningTime.time
            data.doctorName = morningTime.doctorName
            data.emergencyNumber = morningTime.emergencyNumber

            data.schedule_date = date.parseToDate()
            data.typeMedicine = ScheduleModel.TYPE_REGULAR_MEDICINE
            data.status = ScheduleModel.STATUS_ON
            schedules.add(data)
        }

        for (date in dates){
            val data = ScheduleModel()

            data.name = afternoonTime.name
            data.description = afternoonTime.description
            data.time = afternoonTime.time
            data.doctorName = afternoonTime.doctorName
            data.emergencyNumber = afternoonTime.emergencyNumber

            data.schedule_date = date.parseToDate()
            data.typeMedicine = ScheduleModel.TYPE_REGULAR_MEDICINE
            data.status = ScheduleModel.STATUS_ON
            schedules.add(data)
        }

        for (date in dates){
            val data = ScheduleModel()

            data.name = nightTime.name
            data.description = nightTime.description
            data.time = nightTime.time
            data.doctorName = nightTime.doctorName
            data.emergencyNumber = nightTime.emergencyNumber

            data.schedule_date = date.parseToDate()
            data.typeMedicine = ScheduleModel.TYPE_REGULAR_MEDICINE
            data.status = ScheduleModel.STATUS_ON
            schedules.add(data)
        }

        for (scheduleModel in schedules){
            scheduleViewModel.add(scheduleModel, object : MutableLiveData<Long>() {
                override fun setValue(value: Long) {
                    super.setValue(value)
                    Log.e("schedule_save", "id : ${value}")
                }
            })
        }

        Timer().schedule(500){
            startActivity(Intent(context, HomeActivity::class.java))
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
        if (!isMyServiceRunning(context, NotifService::class.java)) {
            val i = Intent(ACTION_RESTART_SERVICE)
            i.setClass(baseContext, AppReceiver::class.java)
            baseContext.sendBroadcast(i)
        }
    }
}