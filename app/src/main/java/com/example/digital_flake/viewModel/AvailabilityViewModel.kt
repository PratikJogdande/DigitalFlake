package com.example.digital_flake.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digital_flake.model.Availability
import com.example.digital_flake.model.Slot
import com.example.digital_flake.repository.UserRepository
import kotlinx.coroutines.launch

class AvailabilityViewModel(private val repository: UserRepository) : ViewModel() {

    private val _availability = MutableLiveData<List<Availability>>()
    val availability: LiveData<List<Availability>> = _availability

    fun fetchAvailability(date: String,selectedSlotId: Int,userId: Int) {
        viewModelScope.launch {
            try {
                val result = repository.getAvailability(date, selectedSlotId,userId)
                _availability.postValue(result ?: emptyList())
            } catch (e: Exception) {

                _availability.postValue(emptyList())
            }
        }
    }
}