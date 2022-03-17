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

class DetailScheduleViewModel : AndroidViewModel {
    private lateinit var repository: DetailScheduleRepository

    constructor(application: Application) : super(application) {
        repository = DetailScheduleRepository(application)
    }

    fun getAllByScheduleId(scheduleIds : List<Int>, result : MutableLiveData<List<DetailScheduleModel>>) {
        viewModelScope.launch {
            result.value = repository.getAllByScheduleId(scheduleIds)
        }
    }

    fun getAllByCurrentDate(now: Date, result : MutableLiveData<List<DetailScheduleModel>>) {
        viewModelScope.launch {
            result.value = repository.getAllByCurrentDate(now)
        }
    }

    fun add(c: DetailScheduleModel, result : MutableLiveData<Long>){
        viewModelScope.launch {
            result.value = repository.add(c)
        }
    }

    fun update(c: DetailScheduleModel) = viewModelScope.launch {
        repository.update(c)
    }

    fun delete(c: DetailScheduleModel) = viewModelScope.launch {
        repository.delete(c)
    }
}