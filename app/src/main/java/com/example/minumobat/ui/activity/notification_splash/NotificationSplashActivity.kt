package com.example.minumobat.ui.activity.notification_splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.minumobat.R

class NotificationSplashActivity : AppCompatActivity() {

    lateinit var text : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_splash)

        text = findViewById(R.id.text)
        text.text = intent.getStringExtra("description")
    }
}