package com.example.minumobat.ui.activity.detail_schedule

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.minumobat.R
import com.example.minumobat.model.detail_schedule_model.DetailScheduleModel
import com.example.minumobat.model.detail_schedule_model.DetailScheduleViewModel
import com.example.minumobat.model.time_picker_model.TimeModel
import com.example.minumobat.ui.adapter.DetailScheduleAdapter
import com.example.minumobat.util.Utils

class DetailScheduleActivity : AppCompatActivity() {
    companion object {
        fun createIntent(ctx : Context, detailScheduleModel: DetailScheduleModel, nextDetailScheduleModel: DetailScheduleModel) : Intent{
            val i = Intent(ctx, DetailScheduleActivity::class.java)
            i.putExtra("detail_schedule", detailScheduleModel)
            i.putExtra("next_detail_schedule", nextDetailScheduleModel)
            return i
        }
    }

    lateinit var context: Context
    
    lateinit var time : TextView
    lateinit var mode : TextView
    
    lateinit var nextTime : TextView
    lateinit var nextMode : TextView
    lateinit var nextScheduleSwitch : SwitchCompat
    lateinit var nextScheduleLayout : CardView

    lateinit var description : TextView
    lateinit var emergencyNumber : TextView
    
    lateinit var detailScheduleModel: DetailScheduleModel
    lateinit var nextDetailScheduleModel: DetailScheduleModel

    lateinit var detailScheduleViewModel: DetailScheduleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_schedule)
        initWidget()
    }

    private fun initWidget() {
        context = this@DetailScheduleActivity

        if ( ! intent.hasExtra("detail_schedule")){
            return
        }
        if ( ! intent.hasExtra("next_detail_schedule")){
            return
        }

        detailScheduleViewModel = ViewModelProvider(context as ViewModelStoreOwner).get(DetailScheduleViewModel::class.java)

        detailScheduleModel = intent.getSerializableExtra("detail_schedule") as DetailScheduleModel
        nextDetailScheduleModel = intent.getSerializableExtra("next_detail_schedule") as DetailScheduleModel

        time = findViewById(R.id.time)
        time.text = TimeModel(detailScheduleModel.hour,detailScheduleModel.minute, 0 ,detailScheduleModel.mode).toString()

        mode = findViewById(R.id.mode)
        mode.text = " ${detailScheduleModel.mode}"

        nextTime = findViewById(R.id.next_schedule_time)
        nextTime.text = TimeModel(nextDetailScheduleModel.hour,nextDetailScheduleModel.minute, 0 ,nextDetailScheduleModel.mode).toString()

        nextMode = findViewById(R.id.next_schedule_mode)
        nextMode.text = " ${nextDetailScheduleModel.mode}"

        val current = TimeModel(nextDetailScheduleModel.hour, nextDetailScheduleModel.minute, 0, nextDetailScheduleModel.mode)

        nextScheduleSwitch = findViewById(R.id.next_schedule_switch)
        nextScheduleSwitch.isChecked = (nextDetailScheduleModel.status == DetailScheduleModel.STATUS_ON)
        checkSwitch(nextDetailScheduleModel,current)
        nextScheduleSwitch.setOnCheckedChangeListener {compoundButton, b ->
            nextDetailScheduleModel.status = if (b) DetailScheduleModel.STATUS_ON else DetailScheduleModel.STATUS_OFF
            detailScheduleViewModel.update(nextDetailScheduleModel)
            checkSwitch(nextDetailScheduleModel,current)
        }

        nextScheduleLayout  = findViewById(R.id.layout_next_schedule)
        nextScheduleLayout.setBackgroundResource(
                if (Utils.isBetween(nextDetailScheduleModel.name, current)) R.drawable.detail_shcedule_adapter_border_shape_selected
                else R.drawable.detail_shcedule_adapter_border_shape)

        description = findViewById(R.id.description)
        description.text = detailScheduleModel.description

        emergencyNumber = findViewById(R.id.phone_number_emergency)
        emergencyNumber.text = detailScheduleModel.emergencyNumber
        emergencyNumber.setOnClickListener {
            startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:${detailScheduleModel.emergencyNumber}")))
        }
    }



    private fun checkSwitch(item : DetailScheduleModel, current : TimeModel){
        val check = (item.status == DetailScheduleModel.STATUS_ON)

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

    override fun onBackPressed() {
        setResult(Activity.RESULT_OK)
        super.onBackPressed()
    }
}