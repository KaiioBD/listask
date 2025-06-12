package com.example.listask.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listask.core.ResultWrapper
import com.example.listask.network.UserRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: UserRepository
): ViewModel() {

    private val _loaderState = MutableLiveData<Boolean>()
    val loaderState: LiveData<Boolean>
        get() = _loaderState
    private val _sessionValid = MutableLiveData<Boolean>()
    val sessionValid: LiveData<Boolean>
        get() = _sessionValid
    private val firebase = FirebaseAuth.getInstance()

    fun requestSignIn(email: String, password: String) {
        _loaderState.value = true
        viewModelScope.launch {
            when (val result = repository.login(email, password)) {
                    is ResultWrapper.Success -> {
                _loaderState.value = false
                _sessionValid.value = true
            }
                is ResultWrapper.Error -> {
                    _loaderState.value = false
                    val errorMessage = result.exception.message
                }
            }
        }
    }

}