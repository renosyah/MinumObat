package com.example.minumobat.dao.detail_schedule

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.minumobat.model.detail_schedule_model.DetailScheduleModel

@Dao
interface DetailScheduleDao {
    @Query("SELECT * FROM detail_schedule WHERE schedule_id = :scheduleId")
    fun getAllByScheduleId(scheduleId : Int): LiveData<List<DetailScheduleModel>>

    @Insert
    suspend fun add(detail : DetailScheduleModel)

    @Update
    suspend  fun update(detail : DetailScheduleModel)

    @Delete
    suspend fun delete(detail : DetailScheduleModel)
}