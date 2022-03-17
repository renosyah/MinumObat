package com.example.minumobat.ui.activity.notification_splash

import android.content.ClipDescription
import android.content.Context
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.TextView
import com.example.minumobat.R
import android.media.RingtoneManager

import android.media.Ringtone
import android.net.Uri
import com.example.minumobat.ui.activity.home.HomeActivity
import com.example.minumobat.ui.activity.schedule_page.SchedulePageActivity
import java.lang.Exception
import java.util.*
import kotlin.concurrent.schedule


class NotificationSplashActivity : AppCompatActivity() {

    companion object {
        fun createIntent(ctx : Context, description: String, time:String) : Intent{
            val i = Intent(ctx, NotificationSplashActivity::class.java)
            i.putExtra("description", description)
            i.putExtra("time", time)
            return i
        }
    }

    lateinit var context: Context

    lateinit var time : TextView
    lateinit var imageRing : ImageView
    lateinit var text : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_splash)
        initWidget()
    }

    private fun initWidget(){
        context = this@NotificationSplashActivity

        text = findViewById(R.id.description_message)
        if (intent.hasExtra("description")) text.text = intent.getStringExtra("description")

        time = findViewById(R.id.time_message)
        if (intent.hasExtra("time")) time.text = intent.getStringExtra("time")

        imageRing = findViewById(R.id.image_ring)
        val anim = imageRing.background as AnimationDrawable
        anim.start()

        Timer().schedule(3500){
            startActivity(SchedulePageActivity.createIntent(context))
            finish()
        }
    }
}