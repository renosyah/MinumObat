package com.example.minumobat.dao.schedule

import androidx.room.*
import com.example.minumobat.model.schedule_model.ScheduleModel
import com.example.minumobat.util.DateConverter
import java.sql.Date

@Dao
@TypeConverters(DateConverter::class)
interface ScheduleDao {

    @Query("SELECT * FROM schedule WHERE (start_date <= :start AND end_date >= :start) OR (start_date <= :end AND end_date >= :end)")
    suspend fun getAllExistingSchedule(start: Date, end: Date): List<ScheduleModel>

    @Query("SELECT * FROM schedule WHERE start_date <= :now AND end_date >= :now")
    suspend fun getAllByCurrentDate(now: Date): List<ScheduleModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(data : ScheduleModel): Long

    @Update
    suspend  fun update(data : ScheduleModel)

    @Delete
    suspend fun delete(data : ScheduleModel)
}