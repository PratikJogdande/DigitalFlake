package com.example.digital_flake.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.digital_flake.repository.UserRepository
import com.example.digital_flake.viewModel.BookingConfirmViewModel
import com.example.digital_flake.viewModel.CreateAccountViewModel

class BookingConfirmViewModelFactory(private val createAccountRepository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(BookingConfirmViewModel::class.java)) {
            BookingConfirmViewModel(createAccountRepository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}