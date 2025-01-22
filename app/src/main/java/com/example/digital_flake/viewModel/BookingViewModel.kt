package com.example.digital_flake.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digital_flake.model.Booking
import com.example.digital_flake.model.BookingResponse
import com.example.digital_flake.network.RetrofitInstance
import com.example.digital_flake.repository.UserRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookingViewModel(private val repository: UserRepository) : ViewModel() {

    private val _bookings = MutableLiveData<List<Booking>>()
    val bookings: LiveData<List<Booking>> = _bookings

    fun fetchBookings(userId: Int) {
        viewModelScope.launch {
            val result = repository.getBookings(userId)
            _bookings.postValue(result)
        }
    }
}