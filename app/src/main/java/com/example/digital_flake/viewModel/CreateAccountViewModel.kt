package com.example.digital_flake.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digital_flake.model.CreateAccountResponse
import com.example.digital_flake.model.LoginResponse
import com.example.digital_flake.repository.UserRepository
import kotlinx.coroutines.launch

class CreateAccountViewModel(private val createAccountRepository: UserRepository) : ViewModel() {

    private val createAccountResult = MutableLiveData<Result<CreateAccountResponse>>()
    val createAccResult: LiveData<Result<CreateAccountResponse>> = createAccountResult

    fun createAccount(email: String, name: String) {
        viewModelScope.launch {
            val result = createAccountRepository.createAccount(email, name)
            createAccountResult.value = result
        }
    }
}