package com.example.listask.view.list.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listask.core.ResultWrapper
import com.example.listask.model.Tarea
import com.example.listask.network.TareaRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.sql.Date
import javax.inject.Inject

@HiltViewModel
class AddTaskViewModel @Inject constructor(

    private val repository: TareaRepository,
    private val firebaseAuth: FirebaseAuth
): ViewModel() {

    private val _tarea = MutableLiveData<List<Tarea>>()
    val tarea: LiveData<List<Tarea>> get() = _tarea

    private val _selectedTarea = MutableLiveData<Tarea>()
    val selectedTarea: LiveData<Tarea> get() = _selectedTarea

    private val _taskAdded = MutableLiveData<Boolean>()
    val taskAdded: LiveData<Boolean> = _taskAdded

    private val _loaderState = MutableLiveData<Boolean>()
    val loaderState: LiveData<Boolean> get() = _loaderState

    private val _operationSuccess = MutableLiveData<Boolean>()
    val operationSuccess: LiveData<Boolean>
    get() = _operationSuccess

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun fetchTareas() {
        viewModelScope.launch {
            _loaderState.value = true
            when (val result = repository.getAllTareas()) {
                is ResultWrapper.Success -> _tarea.value = result.data
                is ResultWrapper.Error -> _error.value = result.exception.message
            }
            _loaderState.value = false
        }
    }

    fun fetchTarea(id: String) {
        viewModelScope.launch {
            _loaderState.value = true
            when (val result = repository.getTarea(id)) {
                is ResultWrapper.Success -> _selectedTarea.value = result.data
                is ResultWrapper.Error -> _error.value = result.exception.message
            }
            _loaderState.value = false
        }
    }

    fun updateTaskInfo(tarea: Tarea) {
        viewModelScope.launch {
            _loaderState.value = true
            when (val result = repository.updateTarea(tarea)) {
                is ResultWrapper.Success -> {
                    _operationSuccess.value = true
                }
                is ResultWrapper.Error -> _error.value = result.exception.message
            }
            _loaderState.value = false
        }
    }

    fun createTaskInfo(tarea: Tarea) {
        _loaderState.value = true
        viewModelScope.launch {
            when (val result = repository.addTarea(tarea.copy(userId = firebaseAuth.currentUser?.uid ?: ""))) {
                is ResultWrapper.Success -> {
                    _operationSuccess.value = true
                    _loaderState.value = false
                }
                is ResultWrapper.Error -> {
                    _error.value = result.exception.message
                    _loaderState.value = false
                }
            }
        }
    }

    fun updateTarea(tarea: Tarea) {
        viewModelScope.launch {
            _loaderState.value = true
            when (val result = repository.updateTarea(tarea)) {
                is ResultWrapper.Success -> Unit // tarea actualizada
                is ResultWrapper.Error -> _error.value = result.exception.message
            }
            _loaderState.value = false
        }
    }

    fun deleteTarea(id: String) {
        viewModelScope.launch {
            _loaderState.value = true
            when (val result = repository.deleteTarea(id)) {
                is ResultWrapper.Success -> Unit // tarea eliminada
                is ResultWrapper.Error -> _error.value = result.exception.message
            }
            _loaderState.value = false
        }
    }


}
