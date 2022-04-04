package com.example.minumobat.ui.activity.schedule_page

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.minumobat.R
import com.example.minumobat.model.detail_schedule_model.DetailScheduleModel
import com.example.minumobat.model.detail_schedule_model.DetailScheduleViewModel
import com.example.minumobat.model.schedule_model.ScheduleModel
import com.example.minumobat.ui.activity.detail_schedule.DetailScheduleActivity
import com.example.minumobat.ui.activity.home.HomeActivity
import com.example.minumobat.ui.adapter.DetailScheduleAdapter
import com.example.minumobat.util.OnSwipeTouchListener
import java.sql.Date
import java.util.*
import kotlin.collections.ArrayList

class SchedulePageActivity : AppCompatActivity() {
    companion object {

        // fungsi static untuk intent
        // jika dari activity lain ingin
        // menjalankan activity ini
        fun createIntent(ctx : Context) : Intent {
            return Intent(ctx, SchedulePageActivity::class.java)
        }
    }

    // deklarasi variabel
    // yang digunakan dalam
    // activity ini
    lateinit var context: Context
    lateinit var title : TextView
    lateinit var imageSwipe : LinearLayout
    lateinit var detailScheduleViewModel: DetailScheduleViewModel
    lateinit var detailScheduleRecycleview : RecyclerView
    lateinit var detailScheduleAdapter: DetailScheduleAdapter

    // fungsi yang akan dijalankan pertama kali saat
    // activity di buat dan diproses serta ditampilkan
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showOnLockScreenAndTurnScreenOn()
        setContentView(R.layout.activity_schedule_page)
        initWidget()
    }

    // fungsi yang akan dijalankan
    // untuk inisialisasi variabel
    // dan memanggil fungsi awal
    @SuppressLint("ClickableViewAccessibility")
    fun initWidget(){
        this.context = this@SchedulePageActivity

        // inisialisasi judul halaman
        // dan latou yang digunakan untuk swipe
        this.title = findViewById(R.id.title_shcedule_page)
        this.imageSwipe = findViewById(R.id.swipe_image)

        // saat layout di swipe keatas oleh user
        // maka akan memanggil activity home
        this.imageSwipe.setOnTouchListener(OnSwipeTouchListener(context) {
            startActivity(HomeActivity.createIntent(context))
            overridePendingTransition(R.anim.slide_up,R.anim.no_change)
        })

        // inisialisasi recycleview data2 schedule
        // sekaligus inisialisasi intance yang digunakan
        // untuk query data2 dari database
        this.detailScheduleRecycleview = findViewById(R.id.detail_schedule_recycleview)
        detailScheduleViewModel = ViewModelProvider(context as ViewModelStoreOwner).get(DetailScheduleViewModel::class.java)

        // memanggil fungsi query
        query()
    }

    // fungsi query untuk
    // melakukan query ke database
    // dan mengambil data schedule
    // berdasarkan tanggal wajtu saat ini
    private fun query(){
        detailScheduleViewModel.getAllByCurrentDate(Date(Calendar.getInstance().time.time), object : MutableLiveData<List<DetailScheduleModel>>() {
            override fun setValue(value: List<DetailScheduleModel>) {
                super.setValue(value)
                val list = ArrayList<DetailScheduleModel>()
                list.addAll(value)
                setAdapter(list)
            }
        })
    }

    // fungsi untuk mengeset adapter
    // daftar schedule yang berhasil
    // diambil dari database
    private fun setAdapter(details : ArrayList<DetailScheduleModel>){
        this.detailScheduleAdapter = DetailScheduleAdapter(context,details, { detail, pos ->

            // saat data di tekan maka
            // akan diarahkan ke menu detail
            val nextPos = if (pos + 1 > detailScheduleAdapter.list.size - 1) 0 else pos + 1
            resultLauncher.launch(DetailScheduleActivity.createIntent(context, detail, detailScheduleAdapter.list[nextPos]))
        },{ detail, pos ->

            // saat tombol switch di klik
            // update data
            detailScheduleViewModel.update(detail)
        })
        this.detailScheduleRecycleview.adapter = detailScheduleAdapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        this.detailScheduleRecycleview.layoutManager = layoutManager
    }

    // variabel yang digunakan untuk
    // menangani saat activity selesai dijalankan
    // dan akan memanggil ulang fungsi query
    val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data
            query()
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
























