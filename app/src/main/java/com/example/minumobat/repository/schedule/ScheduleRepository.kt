package com.example.minumobat.repository.schedule

import com.example.minumobat.db.AppDatabase
import androidx.lifecycle.LiveData
import android.app.Application
import com.example.minumobat.dao.schedule.ScheduleDao
import com.example.minumobat.model.schedule_model.ScheduleModel
import java.sql.Date

class ScheduleRepository {
    private lateinit var sheduleDao: ScheduleDao

    constructor(application: Application) {
        this.sheduleDao = AppDatabase.getDatabase(application).scheduleDao()
    }

    fun getAllByCurrentDate(now: Date): LiveData<List<ScheduleModel>> {
        return sheduleDao.getAllByCurrentDate(now)
    }

    suspend fun add(c: ScheduleModel) {
        sheduleDao.add(c)
    }

    suspend fun update(c: ScheduleModel) {
        sheduleDao.delete(c)
    }

    suspend fun delete(c: ScheduleModel) {
        sheduleDao.delete(c)
    }
}