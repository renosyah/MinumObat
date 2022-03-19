package com.example.minumobat.repository.detail_schedule

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.minumobat.dao.detail_schedule.DetailScheduleDao
import com.example.minumobat.db.AppDatabase
import com.example.minumobat.model.detail_schedule_model.DetailScheduleModel
import com.example.minumobat.model.schedule_model.ScheduleModel
import java.sql.Date


// kelas detail schedule repository
class DetailScheduleRepository {

    // variabel dao untuk query
    private var detailSchedule: DetailScheduleDao

    // konstruktor
    constructor(application: Application) {
        this.detailSchedule = AppDatabase.getDatabase(application).detailScheduleDao()
    }

    // fungsi query ke database untuk
    // mendapatkan data berdasarkan daftar schedule id
    suspend fun getAllByScheduleId(scheduleIds : List<Int>): List<DetailScheduleModel> {
        return detailSchedule.getAllByScheduleId(scheduleIds)
    }

    // fungsi query ke database untuk
    // mendapatkan data berdasarkan tanggal saat ini
    suspend fun getAllByCurrentDate(now: Date): List<DetailScheduleModel> {
        return detailSchedule.getAllByCurrentDate(now)
    }

    // fungsi query ke database untuk
    // insert ke repository
    suspend fun add(c: DetailScheduleModel): Long {
        return detailSchedule.add(c)
    }

    // fungsi query ke database untuk
    // update ke repository
    suspend fun update(c: DetailScheduleModel) {
        detailSchedule.update(c)
    }

    // fungsi query ke database untuk
    // delete ke repository
    suspend fun delete(c: DetailScheduleModel) {
        detailSchedule.delete(c)
    }
}