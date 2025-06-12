/*package com.example.listask.view.list.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listask.core.ResultWrapper
import com.example.listask.model.Tarea
import com.example.listask.network.TareaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskDetailViewModel @Inject constructor(

    private val repository: TareaRepository

): ViewModel() {

    private val _loaderState = MutableLiveData<Boolean>()
    val loaderState: LiveData<Boolean>
        get() = _loaderState

    private val _taskInfo = MutableLiveData<Tarea>()
    val taskInfo: LiveData<Tarea>
        get() = _taskInfo

    fun getTaskInfo(id: String) {
        _loaderState.value = true

        viewModelScope.launch {
            when (val result = repository.getTarea(id)) {
                is ResultWrapper.Success -> {
                    _loaderState.value = false
                    _taskInfo.value = result.data
                }
                is ResultWrapper.Error -> {
                    _loaderState.value = false
                    val errorMessage = result.exception.message
                }
            }
        }
    }

}*/