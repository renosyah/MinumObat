package com.example.minumobat.model.schedule_model

import androidx.room.*
import com.example.minumobat.util.DateConverter
import java.io.Serializable
import java.sql.Date
import java.sql.Time

// class untuk schedule
@Entity(
    tableName = "schedule"
)
class ScheduleModel : Serializable {
    companion object {
        val TYPE_REGULAR_MEDICINE = 111
        val TYPE_INJECTION_MEDICINE = 222

        val STATUS_ON = 1
        val STATUS_OFF = 0
    }

    // primary key dat uid
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "uid")
    var Uid: Long = 0L

    // field tanggal
    @ColumnInfo(name = "schedule_date")
    @TypeConverters(DateConverter::class)
    var schedule_date: Date? = null

    // field type obat
    @ColumnInfo(name = "type_medicine")
    var typeMedicine: Int = TYPE_REGULAR_MEDICINE

    // variabel untuk field nama
    @ColumnInfo(name = "name")
    var name: String = ""

    // variabel untuk field deskripsi
    @ColumnInfo(name = "description")
    var description: String = ""

    // variabel untuk field nama doktor
    @ColumnInfo(name = "doctor_name")
    var doctorName: String = ""

    // variabel untuk field nomor darurat
    @ColumnInfo(name = "emergency_number")
    var emergencyNumber: String = ""

    // variabel untuk field waktu notifikasi
    @ColumnInfo(name = "time")
    @TypeConverters(DateConverter::class)
    var time : Time? = null

    // variabel untuk field status notifikasi
    @ColumnInfo(name = "status")
    var status: Int = STATUS_ON

    constructor() {}

    // fungsi untuk mengecheck
    // field yang dibuthkan valid
    fun isValid() : Boolean{
        return this.name.isNotEmpty() && this.description.isNotEmpty() && this.doctorName.isNotEmpty() && this.emergencyNumber.isNotEmpty()
    }
}