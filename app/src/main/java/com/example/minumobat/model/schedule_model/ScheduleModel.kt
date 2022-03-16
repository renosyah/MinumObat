package com.example.minumobat.model.schedule_model

import androidx.room.*
import com.example.minumobat.util.DateConverter
import java.sql.Date

@Entity(
    tableName = "schedule"
)
class ScheduleModel {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "uid")
    var Uid: Long = 0L

    @ColumnInfo(name = "start_date")
    @TypeConverters(DateConverter::class)
    var StartDate: Date? = null

    @ColumnInfo(name = "end_date")
    @TypeConverters(DateConverter::class)
    var EndDate: Date? = null

    constructor() {}
}