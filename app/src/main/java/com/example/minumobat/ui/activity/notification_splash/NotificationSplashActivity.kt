package com.example.minumobat.ui.activity.notification_splash

import android.content.Context
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.minumobat.R
import com.example.minumobat.ui.activity.schedule_page.SchedulePageActivity
import java.util.*
import kotlin.concurrent.schedule


class NotificationSplashActivity : AppCompatActivity() {

    companion object {

        // fungsi static untuk intent
        // jika dari activity lain ingin
        // menjalankan activity ini
        fun createIntent(ctx : Context, description: String, time:String) : Intent{
            val i = Intent(ctx, NotificationSplashActivity::class.java)
            i.putExtra("description", description)
            i.putExtra("time", time)
            return i
        }
    }

    // deklarasi variabel
    // yang digunakan dalam
    // activity ini
    lateinit var context: Context
    lateinit var time : TextView
    lateinit var imageRing : ImageView
    lateinit var text : TextView

    // fungsi yang akan dijalankan pertama kali saat
    // activity di buat dan diproses serta ditampilkan
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showOnLockScreenAndTurnScreenOn()
        setContentView(R.layout.activity_notification_splash)
        initWidget()
    }

    // fungsi yang akan dijalankan
    // untuk inisialisasi variabel
    // dan memanggil fungsi awal
    private fun initWidget(){
        context = this@NotificationSplashActivity

        // inisialisasi text pesan deskripsi
        // dan di set dari inten yang didapat
        text = findViewById(R.id.description_message)
        if (intent.hasExtra("description")) text.text = intent.getStringExtra("description")

        // inisialisasi text pesan yang berisi waktu
        // dan di set dari inten yang didapat
        time = findViewById(R.id.time_message)
        if (intent.hasExtra("time")) time.text = intent.getStringExtra("time")


        // inisialisasi gambar animasi bell dengan ring
        // lalu memulai animasinya
        imageRing = findViewById(R.id.image_ring)
        val anim = imageRing.background as AnimationDrawable
        anim.start()


        // delay 3,5 detik sebelum masuk
        // ke activity selanjutnya
        // yakni schedule page
        Timer().schedule(3500){
            startActivity(SchedulePageActivity.createIntent(context))
            finish()
        }
    }

    private fun showOnLockScreenAndTurnScreenOn() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            )
        }
    }
}