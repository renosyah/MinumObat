package com.example.minumobat.ui.activity.detail_schedule

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.minumobat.R
import com.example.minumobat.model.schedule_model.ScheduleModel
import com.example.minumobat.model.schedule_model.ScheduleViewModel
import com.example.minumobat.model.time_picker_model.TimeModel
import com.example.minumobat.util.Utils

// kelas activity
// untuk activity detai schedule
// yang akan menampilkan
// data detail dengan waktu serta
// waktu schedule selanjutnya
class DetailScheduleActivity : AppCompatActivity() {
    companion object {

        // fungsi static untuk intent
        // jika dari activity lain ingin
        // menjalankan activity ini
        fun createIntent(ctx : Context, scheduleModel: ScheduleModel, nextScheduleModel: ScheduleModel) : Intent{
            val i = Intent(ctx, DetailScheduleActivity::class.java)
            i.putExtra("schedule", scheduleModel)
            i.putExtra("next_schedule", nextScheduleModel)
            return i
        }
    }

    // deklarasi variabel
    // yang digunakan dalam
    // activity ini
    lateinit var context: Context
    lateinit var time : TextView
    lateinit var mode : TextView
    lateinit var nextTime : TextView
    lateinit var nextMode : TextView
    lateinit var textNextShcedule : TextView
    lateinit var nextScheduleSwitch : SwitchCompat
    lateinit var nextScheduleLayout : CardView
    lateinit var description : TextView
    lateinit var emergencyNumber : TextView
    lateinit var scheduleModel: ScheduleModel
    lateinit var nextScheduleModel: ScheduleModel
    lateinit var scheduleViewModel: ScheduleViewModel

    // fungsi yang akan dijalankan pertama kali saat
    // activity di buat dan diproses serta ditampilkan
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_schedule)
        initWidget()
    }

    // fungsi yang akan dijalankan
    // untuk inisialisasi variabel
    // dan memanggil fungsi awal
    private fun initWidget() {
        context = this@DetailScheduleActivity

        // jika intent yang diminta
        // tidak memiliki nilai variabel
        // yang telah di set maka stop proses
        if ( ! intent.hasExtra("schedule")) return
        if ( ! intent.hasExtra("next_schedule")) return

        // inisialisasi instance yang digunakan
        // sebagai kelas untuk query ke database
        // dengan target tabel database detail schedule
        scheduleViewModel = ViewModelProvider(context as ViewModelStoreOwner).get(ScheduleViewModel::class.java)

        // inisialisasi instance detail schedule
        // dan next detail schedule dari intent
        scheduleModel = intent.getSerializableExtra("schedule") as ScheduleModel
        nextScheduleModel = intent.getSerializableExtra("next_schedule") as ScheduleModel

        // inisialissi text untuk
        // menampilkan waktu schedule
        val current = TimeModel.fromTime(scheduleModel.time)

        // tampilkan text waktu
        time = findViewById(R.id.time)
        time.text = current.toString()

        // tampilkan text mode am atau pm
        mode = findViewById(R.id.mode)
        mode.text = current.mode

        // inisialissi text untuk
        // menampilkan waktu schedule
        // yang selanjutnya
        val nextCurrent = TimeModel.fromTime(nextScheduleModel.time)
        textNextShcedule = findViewById(R.id.text_next_shcedule)
        textNextShcedule.visibility = if (Utils.isBetween(nextScheduleModel.name, nextCurrent)) View.INVISIBLE else View.VISIBLE

        // tampilkan text waktu
        nextTime = findViewById(R.id.next_schedule_time)
        nextTime.text = nextCurrent.toString()

        // tampilkan text mode am atau pm
        nextMode = findViewById(R.id.next_schedule_mode)
        nextMode.text = nextCurrent.mode

        // inisialisasi button switch
        // untuk schedule selanjutnya
        nextScheduleSwitch = findViewById(R.id.next_schedule_switch)
        nextScheduleSwitch.isChecked = (nextScheduleModel.status == ScheduleModel.STATUS_ON)
        checkSwitch(nextScheduleModel,nextCurrent)

        // set callback untuk switch
        // dan simpan update data ke database
        // apa bila switch di geser oleh user
        nextScheduleSwitch.setOnCheckedChangeListener {compoundButton, b ->
            nextScheduleModel.status = if (b) ScheduleModel.STATUS_ON else ScheduleModel.STATUS_OFF
            scheduleViewModel.update(nextScheduleModel)
            checkSwitch(nextScheduleModel,nextCurrent)
        }

        // tampilkan warna latar belakang
        // jika detail schedule selanjutnya masuk dalam jangka waktu
        nextScheduleLayout  = findViewById(R.id.layout_next_schedule)
        nextScheduleLayout.setBackgroundResource(
                if (Utils.isBetween(nextScheduleModel.name, nextCurrent)) R.drawable.detail_shcedule_adapter_border_shape_selected
                else R.drawable.detail_shcedule_adapter_border_shape)

        // menampilkan deskripsi
        description = findViewById(R.id.description)
        description.text = scheduleModel.description

        // menampilkan nomor telp serta
        // nama dokter, apabila di klik
        // maka akan diarahkan ke dial pad
        emergencyNumber = findViewById(R.id.phone_number_emergency)
        emergencyNumber.text = "${scheduleModel.doctorName} ${scheduleModel.emergencyNumber}"
        emergencyNumber.setOnClickListener {
            startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:${scheduleModel.emergencyNumber}")))
        }
    }

    // fungsi untuk mengecheck warna switch
    // berdasarkan status flag data waktu
    // schedul selanjutnya apakh on atau off
    private fun checkSwitch(item : ScheduleModel, current : TimeModel){
        val check = (item.status == ScheduleModel.STATUS_ON)
        var thumbDrawable = ContextCompat.getDrawable(context,R.drawable.switch_rounded_thumb_disable)
        var trackDrawable = ContextCompat.getDrawable(context, R.drawable.switch_rounded_track_disable)
        if (Utils.isBetween(item.name, current)){
            if (check) {
                thumbDrawable = ContextCompat.getDrawable(context,R.drawable.switch_rounded_thumb_enable_selected)
                trackDrawable = ContextCompat.getDrawable(context, R.drawable.switch_rounded_track_enable_selected)
            }
        } else {
            if (check) {
                thumbDrawable = ContextCompat.getDrawable(context,R.drawable.switch_rounded_thumb_enable)
                trackDrawable = ContextCompat.getDrawable(context, R.drawable.switch_rounded_track_enable)
            }
        }
        nextScheduleSwitch.thumbDrawable = thumbDrawable
        nextScheduleSwitch.trackDrawable = trackDrawable
    }

    // fungsi yang akan dijalankan
    // saat user mengklik tombol kembali
    override fun onBackPressed() {
        setResult(Activity.RESULT_OK)
        super.onBackPressed()
    }
}