package com.example.digital_flake.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digital_flake.model.Availability
import com.example.digital_flake.model.BookingConfirm
import com.example.digital_flake.model.BookingConfirmResponse
import com.example.digital_flake.model.BookingResponse
import com.example.digital_flake.model.CreateAccountResponse
import com.example.digital_flake.repository.UserRepository
import kotlinx.coroutines.launch

class BookingConfirmViewModel(private val createAccountRepository: UserRepository) : ViewModel() {

    private val createAccountResult = MutableLiveData<Result<BookingConfirmResponse>>()
    val createAccResult: LiveData<Result<BookingConfirmResponse>> get() = createAccountResult

    fun createAccount(date: String, slot_id: Int, workspace_id: Int, type: Int) {
        viewModelScope.launch {
            val result = createAccountRepository.createBookingConfirm(date,slot_id,workspace_id,type)
            createAccountResult.value = result
        }
    }
}
//private val bookingRepository = UserRepository()
//
//private val _bookingResponse = MutableLiveData<BookingResponse?>()
//val bookingResponse: LiveData<BookingResponse?> get() = _bookingResponse
//
//fun confirmBooking(date: String, slot_id: Int, workspace_id: Int, type: Int) {
//    // Call the repository function inside a coroutine
//    viewModelScope.launch {
//        val response = bookingRepository.getBookingConfirm(date, slot_id, workspace_id, type)
//        _bookingResponse.value = response
//    }
//}