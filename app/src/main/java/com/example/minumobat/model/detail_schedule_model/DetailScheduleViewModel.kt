package com.example.minumobat.model.detail_schedule_model

import androidx.lifecycle.AndroidViewModel
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.minumobat.repository.detail_schedule.DetailScheduleRepository
import androidx.lifecycle.viewModelScope
import com.example.minumobat.model.schedule_model.ScheduleModel
import kotlinx.coroutines.launch
import java.sql.Date

// class untuk view model detail schedule
class DetailScheduleViewModel : AndroidViewModel {

    // variabel repository
    private lateinit var repository: DetailScheduleRepository

    // fungsi kontruktor
    constructor(application: Application) : super(application) {
        repository = DetailScheduleRepository(application)
    }

    // fungsi query ke repository untuk
    // mendapatkan data berdasarkan daftar schedule id
    fun getAllByScheduleId(scheduleIds : List<Int>, result : MutableLiveData<List<DetailScheduleModel>>) {
        viewModelScope.launch {
            result.value = repository.getAllByScheduleId(scheduleIds)
        }
    }

    // fungsi query ke repository untuk
    // mendapatkan data berdasarkan tanggal saat ini
    fun getAllByCurrentDate(now: Date, result : MutableLiveData<List<DetailScheduleModel>>) {
        viewModelScope.launch {
            result.value = repository.getAllByCurrentDate(now)
        }
    }

    // fungsi query ke repository untuk
    // insert ke repository
    fun add(c: DetailScheduleModel, result : MutableLiveData<Long>){
        viewModelScope.launch {
            result.value = repository.add(c)
        }
    }

    // fungsi query ke repository untuk
    // update ke repository
    fun update(c: DetailScheduleModel) = viewModelScope.launch {
        repository.update(c)
    }

    // fungsi query ke repository untuk
    // delete ke repository
    fun delete(c: DetailScheduleModel) = viewModelScope.launch {
        repository.delete(c)
    }
}