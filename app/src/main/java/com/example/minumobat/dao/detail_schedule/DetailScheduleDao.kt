package com.example.minumobat.dao.detail_schedule

import androidx.room.*
import com.example.minumobat.model.detail_schedule_model.DetailScheduleModel

@Dao
interface DetailScheduleDao {

    @Query("SELECT * FROM detail_schedule WHERE schedule_id = :scheduleId")
    suspend fun getAllByScheduleId(scheduleId : Int): List<DetailScheduleModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(detail : DetailScheduleModel) : Long

    @Update
    suspend fun update(detail : DetailScheduleModel)

    @Delete
    suspend fun delete(detail : DetailScheduleModel)
}