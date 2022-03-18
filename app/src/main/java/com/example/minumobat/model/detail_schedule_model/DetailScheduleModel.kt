package com.example.minumobat.model.detail_schedule_model

import androidx.room.*
import com.example.minumobat.model.schedule_model.ScheduleModel
import com.example.minumobat.util.DateConverter
import java.io.Serializable
import java.sql.Time

@Entity(
    tableName = "detail_schedule",
    foreignKeys = [
    ForeignKey(
        entity = ScheduleModel::class,
        parentColumns = ["uid"],
        childColumns = ["schedule_id"],
        onDelete = ForeignKey.CASCADE
    )
])

class DetailScheduleModel : Serializable{
    companion object {
        val STATUS_ON = 1
        val STATUS_OFF = 0
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "uid")
    var uid: Long = 0L

    @ColumnInfo(name = "schedule_id")
    var scheduleID : Long = 0L

    @ColumnInfo(name = "name")
    var name: String = ""

    @ColumnInfo(name = "description")
    var description: String = ""

    @ColumnInfo(name = "doctor_name")
    var doctorName: String = ""

    @ColumnInfo(name = "emergency_number")
    var emergencyNumber: String = ""

    @ColumnInfo(name = "time")
    @TypeConverters(DateConverter::class)
    var time : Time? = null

    @ColumnInfo(name = "status")
    var status: Int = 0

    constructor() {}

    fun isValid() : Boolean{
        return this.name.isNotEmpty() && this.description.isNotEmpty() && this.doctorName.isNotEmpty() && this.emergencyNumber.isNotEmpty()
    }
}