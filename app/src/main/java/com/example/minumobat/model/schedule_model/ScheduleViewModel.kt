package com.example.minumobat.model.schedule_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.minumobat.repository.schedule.ScheduleRepository
import kotlinx.coroutines.launch
import java.sql.Date

class ScheduleViewModel : AndroidViewModel {
    private lateinit var repository: ScheduleRepository

    constructor(application: Application) : super(application) {
        repository = ScheduleRepository(application)
    }

    fun getAllByCurrentDate(now: Date): LiveData<List<ScheduleModel>> {
        return repository.getAllByCurrentDate(now)
    }

    fun add(c: ScheduleModel, result : MutableLiveData<Long>){
        viewModelScope.launch {
            result.value = repository.add(c)
        }
    }

    fun update(c: ScheduleModel) = viewModelScope.launch {
        repository.update(c)
    }

    fun delete(c: ScheduleModel) = viewModelScope.launch {
        repository.delete(c)
    }
}