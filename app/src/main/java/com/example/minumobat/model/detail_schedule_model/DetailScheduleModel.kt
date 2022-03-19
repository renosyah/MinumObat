package com.example.minumobat.model.detail_schedule_model

import androidx.room.*
import com.example.minumobat.model.schedule_model.ScheduleModel
import com.example.minumobat.util.DateConverter
import java.io.Serializable
import java.sql.Time

// class untuk detail schedule
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

    // primary key dat uid
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "uid")
    var uid: Long = 0L

    // foreign key schedule_id
    // untuk refrensi data ke tabel schedule
    @ColumnInfo(name = "schedule_id")
    var scheduleID : Long = 0L

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
    var status: Int = 0

    constructor() {}

    // fungsi untuk mengecheck
    // field yang dibuthkan valid
    fun isValid() : Boolean{
        return this.name.isNotEmpty() && this.description.isNotEmpty() && this.doctorName.isNotEmpty() && this.emergencyNumber.isNotEmpty()
    }
}