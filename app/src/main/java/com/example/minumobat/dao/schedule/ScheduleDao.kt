package com.example.minumobat.dao.schedule

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.minumobat.model.schedule_model.ScheduleModel
import com.example.minumobat.util.DateConverter
import java.sql.Date

@Dao
@TypeConverters(DateConverter::class)
interface ScheduleDao {
    @Query("SELECT * FROM schedule WHERE start_date <= :now AND end_date >= :now")

    fun getAllByCurrentDate(now: Date): LiveData<List<ScheduleModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(data : ScheduleModel): Long

    @Update
    suspend  fun update(data : ScheduleModel)

    @Delete
    suspend fun delete(data : ScheduleModel)
}