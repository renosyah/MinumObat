package com.example.minumobat.repository.detail_schedule

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.minumobat.dao.detail_schedule.DetailScheduleDao
import com.example.minumobat.db.AppDatabase
import com.example.minumobat.model.detail_schedule_model.DetailScheduleModel

class DetailScheduleRepository {
    private var detailSchedule: DetailScheduleDao

    constructor(application: Application) {
        this.detailSchedule = AppDatabase.getDatabase(application).detailScheduleDao()
    }

    fun getAllByScheduleId(scheduleId : Int): LiveData<List<DetailScheduleModel>> {
        return detailSchedule.getAllByScheduleId(scheduleId)
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