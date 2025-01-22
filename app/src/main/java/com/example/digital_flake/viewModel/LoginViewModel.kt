package com.example.digital_flake.viewModel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digital_flake.model.LoginRequest
import com.example.digital_flake.model.LoginResponse
import com.example.digital_flake.network.RetrofitInstance
import com.example.digital_flake.repository.UserRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class LoginViewModel (private val loginRepository: UserRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<Result<LoginResponse>>()
    val loginResult: LiveData<Result<LoginResponse>> = _loginResult

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val result = loginRepository.login(email, password)
            _loginResult.value = result
        }
    }
}