package com.example.digital_flake.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digital_flake.model.Booking
import com.example.digital_flake.model.Slot
import com.example.digital_flake.repository.UserRepository
import kotlinx.coroutines.launch

class SlotViewModel(private val repository: UserRepository) : ViewModel() {

    private val _slots = MutableLiveData<List<Slot>>()
    val slots: LiveData<List<Slot>> = _slots

    fun fetchSlots(date: String) {
        viewModelScope.launch {
            try {
                val result = repository.getSlots(date)
                _slots.postValue(result ?: emptyList()) // Return empty list if null
            } catch (e: Exception) {
                // Handle error appropriately (e.g., show a toast or log it)
                _slots.postValue(emptyList())  // Return an empty list in case of error
            }
        }
    }
}
