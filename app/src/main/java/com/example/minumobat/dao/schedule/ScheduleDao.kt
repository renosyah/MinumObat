package com.example.minumobat.dao.schedule

import androidx.room.*
import com.example.minumobat.model.schedule_model.ScheduleModel
import com.example.minumobat.util.DateConverter
import java.sql.Date

@Dao
@TypeConverters(DateConverter::class)
interface ScheduleDao {

    // fungsi inteface untuk query data schedule dengan bulan dan tahun
    @Query("SELECT * FROM schedule WHERE date(datetime(schedule_date / 1000 , 'unixepoch')) = date(datetime(:current / 1000 , 'unixepoch'))")
    suspend fun getCurrent(current: Date): List<ScheduleModel>

    // fungsi inteface untuk query data schedule dengan bulan dan tahun
    @Query("SELECT * FROM schedule WHERE date(datetime(schedule_date / 1000 , 'unixepoch')) = date(datetime(:current / 1000 , 'unixepoch')) AND type_medicine = :typeMedicine")
    suspend fun getCurrentByTypeMedicine(current: Date, typeMedicine : Int): List<ScheduleModel>

    // fungsi inteface untuk query insert data detail schedule
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(data : ScheduleModel): Long

    // fungsi inteface untuk query update data detail schedule
    @Update
    suspend  fun update(data : ScheduleModel)

    // fungsi inteface untuk query delete data detail schedule
    @Delete
    suspend fun delete(data : ScheduleModel)
}