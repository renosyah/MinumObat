package com.example.minumobat.repository.schedule

import com.example.minumobat.db.AppDatabase
import androidx.lifecycle.LiveData
import android.app.Application
import com.example.minumobat.dao.schedule.ScheduleDao
import com.example.minumobat.model.schedule_model.ScheduleModel
import java.sql.Date

// kelas schedule repository
class ScheduleRepository {

    // variabel dao untuk query
    private lateinit var sheduleDao: ScheduleDao

    // konstruktor
    constructor(application: Application) {
        this.sheduleDao = AppDatabase.getDatabase(application).scheduleDao()
    }

    // fungsi query ke database untuk
    // mendapatkan data berdasarkan tanggal
    suspend fun getCurrent(current: Date): List<ScheduleModel> {
        return sheduleDao.getCurrent(current)
    }

    // fungsi query ke database untuk
    // mendapatkan data berdasarkan tanggal
    suspend fun getCurrentByTypeMedicine(current: Date, typeMedicine : Int): List<ScheduleModel> {
        return sheduleDao.getCurrentByTypeMedicine(current, typeMedicine)
    }

    // fungsi query ke database untuk
    // mendapatkan data berdasarkan tanggal
    suspend fun getAll(): List<ScheduleModel> {
        return sheduleDao.getAll()
    }

    // fungsi query ke database untuk
    // insert ke repository
    suspend fun add(c: ScheduleModel): Long {
        return sheduleDao.add(c)
    }

    // fungsi query ke database untuk
    // update ke repository
    suspend fun update(c: ScheduleModel) {
        sheduleDao.update(c)
    }

    // fungsi query ke database untuk
    // delete ke repository
    suspend fun delete(c: ScheduleModel) {
        sheduleDao.delete(c)
    }
}