package com.example.minumobat.model.schedule_model

import androidx.room.*
import com.example.minumobat.util.DateConverter
import java.sql.Date

// class untuk schedule
@Entity(
    tableName = "schedule"
)
class ScheduleModel {

    // primary key dat uid
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "uid")
    var Uid: Long = 0L

    // field tanggal mulai
    @ColumnInfo(name = "start_date")
    @TypeConverters(DateConverter::class)
    var startDate: Date? = null

    // field tanggal berakhir
    @ColumnInfo(name = "end_date")
    @TypeConverters(DateConverter::class)
    var endDate: Date? = null

    constructor() {}
}