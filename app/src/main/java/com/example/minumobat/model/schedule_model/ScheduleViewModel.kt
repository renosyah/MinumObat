package com.example.minumobat.model.schedule_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.minumobat.repository.schedule.ScheduleRepository
import kotlinx.coroutines.launch
import java.sql.Date

// class untuk view model schedule
class ScheduleViewModel : AndroidViewModel {

    // variabel repository
    private lateinit var repository: ScheduleRepository

    // fungsi kontruktor
    constructor(application: Application) : super(application) {
        repository = ScheduleRepository(application)
    }

    // fungsi query ke repository untuk
    // mendapatkan data berdasarkan tanggal saat ini
    fun getAllByCurrentDate(now: Date, result: MutableLiveData<List<ScheduleModel>>) {
        viewModelScope.launch {
            result.value = repository.getAllByCurrentDate(now)
        }
    }

    // fungsi query ke repository untuk
    // mendapatkan data berdasarkan tanggal  mulai dan berakhir
    fun getAllExistingSchedule(start: Date, end: Date, result: MutableLiveData<List<ScheduleModel>>) {
        viewModelScope.launch {
            result.value = repository.getAllExistingSchedule(start, end)
        }
    }

    // fungsi query ke repository untuk
    // insert ke repository
    fun add(c: ScheduleModel, result : MutableLiveData<Long>){
        viewModelScope.launch {
            result.value = repository.add(c)
        }
    }

    // fungsi query ke repository untuk
    // update ke repository
    fun update(c: ScheduleModel) = viewModelScope.launch {
        repository.update(c)
    }

    // fungsi query ke repository untuk
    // delete ke repository
    fun delete(c: ScheduleModel) = viewModelScope.launch {
        repository.delete(c)
    }
}