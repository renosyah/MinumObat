package com.example.minumobat.repository.detail_schedule

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.minumobat.dao.detail_schedule.DetailScheduleDao
import com.example.minumobat.db.AppDatabase
import com.example.minumobat.model.detail_schedule_model.DetailScheduleModel
import com.example.minumobat.model.schedule_model.ScheduleModel
import java.sql.Date

class DetailScheduleRepository {
    private var detailSchedule: DetailScheduleDao

    constructor(application: Application) {
        this.detailSchedule = AppDatabase.getDatabase(application).detailScheduleDao()
    }

    suspend fun getAllByScheduleId(scheduleIds : List<Int>): List<DetailScheduleModel> {
        return detailSchedule.getAllByScheduleId(scheduleIds)
    }

    suspend fun getAllByCurrentDate(now: Date): List<DetailScheduleModel> {
        return detailSchedule.getAllByCurrentDate(now)
    }

    suspend fun add(c: DetailScheduleModel): Long {
        return detailSchedule.add(c)
    }

    suspend fun update(c: DetailScheduleModel) {
        detailSchedule.delete(c)
    }

    suspend fun delete(c: DetailScheduleModel) {
        detailSchedule.delete(c)
    }
}