package com.example.minumobat.ui.activity.schedule_page

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        fun createIntent(ctx : Context) : Intent {
            return Intent(ctx, SchedulePageActivity::class.java)
        }
    }

    lateinit var context: Context
    lateinit var title : TextView
    lateinit var imageSwipe : LinearLayout

    lateinit var detailScheduleViewModel: DetailScheduleViewModel
    lateinit var detailScheduleRecycleview : RecyclerView
    lateinit var detailScheduleAdapter: DetailScheduleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_page)
        initWidget()
    }

    @SuppressLint("ClickableViewAccessibility")
    fun initWidget(){
        this.context = this@SchedulePageActivity

        this.title = findViewById(R.id.title_shcedule_page)
        this.imageSwipe = findViewById(R.id.swipe_image)
        this.imageSwipe.setOnTouchListener(OnSwipeTouchListener(context) {
            startActivity(HomeActivity.createIntent(context))
            overridePendingTransition(R.anim.slide_up,R.anim.no_change)
        })

        this.detailScheduleRecycleview = findViewById(R.id.detail_schedule_recycleview)
        detailScheduleViewModel = ViewModelProvider(context as ViewModelStoreOwner).get(DetailScheduleViewModel::class.java)

        query()
    }

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

    private fun setAdapter(details : ArrayList<DetailScheduleModel>){
        this.detailScheduleAdapter = DetailScheduleAdapter(context,details, { detail, pos ->
            val nextPos = if (pos + 1 > detailScheduleAdapter.list.size - 1) 0 else pos + 1
            resultLauncher.launch(DetailScheduleActivity.createIntent(context, detail, detailScheduleAdapter.list[nextPos]))

        },{ detail, pos ->
            detailScheduleViewModel.update(detail)
        })
        this.detailScheduleRecycleview.adapter = detailScheduleAdapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        this.detailScheduleRecycleview.layoutManager = layoutManager
    }

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data
            query()
        }
    }
}
























