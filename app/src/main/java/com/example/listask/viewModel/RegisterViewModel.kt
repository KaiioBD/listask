package com.example.listask.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listask.core.ResultWrapper
import com.example.listask.network.UserRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class RegisterViewModel: ViewModel() {

    private val repository = UserRepository()

    private val _loaderState = MutableLiveData<Boolean>()
    val loaderState: LiveData<Boolean> get() = _loaderState

    private val _createdUser = MutableLiveData<Boolean>()
    val createdUser: LiveData<Boolean> get() = _createdUser

    private val _signUpSuccess = MutableLiveData<Boolean>()
    val signUpSuccess: LiveData<Boolean> get() = _signUpSuccess

    //private val firebase = FirebaseAuth.getInstance()

    fun requestSignUp(email: String, password: String) {
        _loaderState.value = true
        _signUpSuccess.value = false
        viewModelScope.launch {
            when(val result = repository.register(email, password)) {
                    is ResultWrapper.Success -> {
                _loaderState.value = false
                _createdUser.value = true
                _signUpSuccess.value = true
            }
                is ResultWrapper.Error -> {
                    _loaderState.value = false
                    val errorMessage = result.exception.message
                }
            }
        }
    }
}