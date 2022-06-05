package com.example.minumobat.ui.activity.landing

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import com.example.minumobat.R
import com.example.minumobat.ui.activity.home.HomeActivity
import com.example.minumobat.ui.activity.home_inject.HomeInjectActivity

class LandingActivity : AppCompatActivity() {

    // deklarasi variabel
    // yang digunakan dalam
    // activity ini
    lateinit var context: Context
    lateinit var layoutRegular : LinearLayout
    lateinit var layoutInjection : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)
        initWidget()
    }

    // fungsi yang akan dijalankan
    // untuk inisialisasi variabel
    // dan memanggil fungsi awal
    private fun initWidget() {
        context = this@LandingActivity

        layoutRegular = findViewById(R.id.layout_regular)
        layoutRegular.setOnClickListener {
            startActivity(HomeActivity.createIntent(context))
        }

        layoutInjection = findViewById(R.id.layout_injection)
        layoutInjection.setOnClickListener {
            startActivity(HomeInjectActivity.createIntent(context))
        }
    }
}