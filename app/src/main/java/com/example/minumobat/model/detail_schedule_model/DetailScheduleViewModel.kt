package com.example.minumobat.model.detail_schedule_model

import androidx.lifecycle.AndroidViewModel
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.minumobat.repository.detail_schedule.DetailScheduleRepository
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class DetailScheduleViewModel : AndroidViewModel {
    private lateinit var repository: DetailScheduleRepository

    constructor(application: Application) : super(application) {
        repository = DetailScheduleRepository(application)
    }

    fun getAllByScheduleId(scheduleId : Int): LiveData<List<DetailScheduleModel>>{
        return repository.getAllByScheduleId(scheduleId)
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