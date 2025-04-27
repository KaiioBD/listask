package com.example.listask.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class RegisterViewModel: ViewModel() {

    private val _loaderState = MutableLiveData<Boolean>()
    val loaderState: LiveData<Boolean> get() = _loaderState

    private val _signUpSuccess = MutableLiveData<Boolean>()
    val signUpSuccess: LiveData<Boolean> get() = _signUpSuccess

    private val firebase = FirebaseAuth.getInstance()

    fun requestSignUp(email: String, password: String) {
        _loaderState.value = true
        viewModelScope.launch {
            try {
                val result = firebase.createUserWithEmailAndPassword(email, password).await()
                _loaderState.value = false
                result.user?.let {
                    Log.i("Firebase", "Se pudo crear el usuario")
                    _signUpSuccess.value = true
                } ?: run {
                    Log.e("Firebase", "El usuario es null")
                    _signUpSuccess.value = false
                }
            } catch (e: Exception) {
                Log.e("Firebase", "Error al crear el usuario", e)
                _loaderState.value = false
                _signUpSuccess.value = false
            }
        }
    }
}