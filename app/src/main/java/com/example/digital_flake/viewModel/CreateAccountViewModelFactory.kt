package com.example.digital_flake.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.digital_flake.repository.UserRepository

class CreateAccountViewModelFactory(private val createAccountRepository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CreateAccountViewModel::class.java)) {
            CreateAccountViewModel(createAccountRepository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}