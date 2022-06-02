package com.example.minumobat.model.schedule_model

import androidx.room.*
import com.example.minumobat.util.DateConverter
import java.sql.Date

// class untuk schedule
@Entity(
    tableName = "schedule"
)
class ScheduleModel {
    companion object {
        val TYPE_REGULAR_MEDICINE = 1
        val TYPE_INJECTION_MEDICINE = 2
    }

    // primary key dat uid
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "uid")
    var Uid: Long = 0L

    // field tanggal
    @ColumnInfo(name = "date")
    @TypeConverters(DateConverter::class)
    var date: Date? = null

    // field type obat
    @ColumnInfo(name = "type_medicine")
    var typeMedicine: Int = 0

    constructor() {}
}