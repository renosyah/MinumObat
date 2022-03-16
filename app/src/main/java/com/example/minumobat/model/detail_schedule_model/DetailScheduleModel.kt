package com.example.minumobat.model.detail_schedule_model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.minumobat.model.schedule_model.ScheduleModel

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

class DetailScheduleModel {
    companion object {
        val STATUS_ON = 1
        val STATUS_OFF = 0
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "uid")
    var Uid: Long = 0L

    @ColumnInfo(name = "schedule_id")
    var ScheduleID : Long = 0L

    @ColumnInfo(name = "name")
    var Name: String = ""

    @ColumnInfo(name = "description")
    var Description: String = ""

    @ColumnInfo(name = "hour")
    var Hour: Int = 0

    @ColumnInfo(name = "minute")
    var Minute: Int = 0

    @ColumnInfo(name = "status")
    var Status: Int = 0

    constructor() {}
}