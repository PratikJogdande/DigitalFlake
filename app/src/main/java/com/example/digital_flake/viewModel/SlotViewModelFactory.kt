package com.example.digital_flake.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.digital_flake.repository.UserRepository

class SlotViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SlotViewModel::class.java)) {
            return SlotViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}