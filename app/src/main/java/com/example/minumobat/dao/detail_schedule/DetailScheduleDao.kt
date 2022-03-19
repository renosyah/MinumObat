package com.example.minumobat.dao.detail_schedule

import androidx.room.*
import com.example.minumobat.model.detail_schedule_model.DetailScheduleModel
import com.example.minumobat.util.DateConverter
import java.sql.Date

@Dao
@TypeConverters(DateConverter::class)
interface DetailScheduleDao {

    // fungsi inteface untuk query data detail schedule dengan daftar id schedule
    @Query("SELECT * FROM detail_schedule WHERE schedule_id IN (:scheduleIds)")
    suspend fun getAllByScheduleId(scheduleIds : List<Int>): List<DetailScheduleModel>

    // fungsi inteface untuk query data detail schedule berdasarkan tanggal awal dan tanggal akhir schedule
    @Query("SELECT ds.* FROM detail_schedule ds LEFT JOIN schedule s ON ds.schedule_id = s.uid WHERE s.start_date <= :now AND s.end_date >= :now")
    suspend fun getAllByCurrentDate(now: Date): List<DetailScheduleModel>

    // fungsi inteface untuk query insert data detail schedule
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(detail : DetailScheduleModel) : Long

    // fungsi inteface untuk query update data detail schedule
    @Update
    suspend fun update(detail : DetailScheduleModel)

    // fungsi inteface untuk query delete data detail schedule
    @Delete
    suspend fun delete(detail : DetailScheduleModel)
}