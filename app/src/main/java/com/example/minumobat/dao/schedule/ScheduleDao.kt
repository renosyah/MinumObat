package com.example.minumobat.dao.schedule

import androidx.room.*
import com.example.minumobat.model.schedule_model.ScheduleModel
import com.example.minumobat.util.DateConverter
import java.sql.Date

@Dao
@TypeConverters(DateConverter::class)
interface ScheduleDao {

    // fungsi inteface untuk query data schedule berdasarkan tanggal awal dan tanggal akhir schedule
    // untuk memastikan apakah data range intersect dengan data yang ada di database
    @Query("SELECT * FROM schedule WHERE (start_date <= :start AND end_date >= :start) OR (start_date <= :end AND end_date >= :end)")
    suspend fun getAllExistingSchedule(start: Date, end: Date): List<ScheduleModel>

    // fungsi inteface untuk query data schedule berdasarkan tanggal awal dan tanggal akhir schedule
    @Query("SELECT * FROM schedule WHERE start_date <= :now AND end_date >= :now")
    suspend fun getAllByCurrentDate(now: Date): List<ScheduleModel>

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