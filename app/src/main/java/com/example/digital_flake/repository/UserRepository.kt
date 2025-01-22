package com.example.digital_flake.repository

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import com.example.digital_flake.api.ApiService
import com.example.digital_flake.model.Availability
import com.example.digital_flake.model.Booking
import com.example.digital_flake.model.BookingConfirm
import com.example.digital_flake.model.BookingConfirmResponse
import com.example.digital_flake.model.BookingResponse
import com.example.digital_flake.model.CreateAccountResponse
import com.example.digital_flake.model.LoginResponse
import com.example.digital_flake.model.Slot
import com.example.digital_flake.model.SlotResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository(private val apiService: ApiService, private val context: Context) {

    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val response = apiService.login(email, password)
            if (response.isSuccessful) {
                val loginResponse = response.body()!!
                val userId = loginResponse.user_id
                Log.i("Login", "User ID: $userId")
                saveUserIdToSharedPreferences(userId)
                Result.success(response.body()!!)

            } else {
                Result.failure(Exception("Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    private fun saveUserIdToSharedPreferences(userId: Int) {
        // Access SharedPreferences
        val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        // Save the userId in SharedPreferences
        sharedPreferences.edit().apply {
            putInt("user_id", userId)
            apply()
        }
    }

     suspend fun createAccount(email: String, name: String): Result<CreateAccountResponse> {
        return try {
            val response = apiService.createAccount(email, name)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createBookingConfirm(date: String, slot_id: Int, workspace_id: Int, type: Int): Result<BookingConfirmResponse> {
        return try {
            val response = apiService.createBookingConfirm(date, slot_id, workspace_id, type)
            if (response.isSuccessful) {
                Result.success(response.body()!!)

            } else {
                Result.failure(Exception("Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun getBookings(userId: Int): List<Booking>? {
        val response = apiService.getBookings(userId)
        if (response.isSuccessful) {
            return response.body()?.bookings
        }
        return null
    }

    suspend fun getSlots(date: String): List<Slot>? {
        return try {
            val response = apiService.getSlots(date)
            if (response.isSuccessful) {
                Log.d("API_RESPONSE", "Response body: ${response.body()}")
                response.body()?.slots ?: emptyList()
            } else {
                Log.e("API_ERROR", "Error response: ${response.errorBody()}")
                null
            }
        } catch (e: Exception) {
            Log.e("API_ERROR", "Network exception: ${e.message}")
            null
        }
    }


    suspend fun getAvailability(date: String,selectedSlotId: Int,userId: Int): List<Availability>? {
        return try {
            val response = apiService.getAvailability(date, selectedSlotId, userId)
            Log.i(TAG, "getAvailability: "+date+" "+selectedSlotId+" "+userId)
            if (response.isSuccessful) {
                Log.d("API_RESPONSE", "Response body: ${response.body()}")
                response.body()?.availability ?: emptyList()
            } else {
                Log.e("API_ERROR", "Error response: ${response.errorBody()}")
                null
            }
        } catch (e: Exception) {
            Log.e("API_ERROR", "Network exception: ${e.message}")
            null
        }

    }

//    suspend fun getBookingConfirm(date: String, slot_id: Int, workspace_id: Int, type: Int): List<BookingConfirm>? {
//        return try {
//            val response = apiService.getBookingConfirm(date, slot_id, workspace_id, type)
//            Log.i(TAG, "getAvailability: "+date+" "+slot_id+" "+workspace_id+"  "+type)
//            if (response.isSuccessful) {
//                Log.d("API_RESPONSE", "Response body: ${response.body()}")
//                response.body()?.message ?: emptyList()
//            } else {
//                Log.e("API_ERROR", "Error response: ${response.errorBody()}")
//                null
//            }
//        } catch (e: Exception) {
//            Log.e("API_ERROR", "Network exception: ${e.message}")
//            null
//        }
//
//    }


    // private val apiService = RetrofitInstance.create()

//    suspend fun confirmBooking(date: String, slot_id: Int, workspace_id: Int, type: Int): BookingResponse? {
//        val response = apiService.confirmBooking(date, slot_id, workspace_id, type)
//        return if (response.isSuccessful) {
//            response.body()  // Return the BookingResponse from the body
//        } else {
//            null  // Handle the failure
//        }
//    }

//    suspend fun getSlots(date: String): Response<SlotResponse>? {
//        return apiService.getSlots(date)
//    }

//    suspend fun getSlots(date: String): List<Slot>? {
//        val response = apiService.getSlots(date)
//        if (response.isSuccessful) {
//            return response.body()?.slots
//        }
//        return null
//    }

//    fun createAccount(email: String, name: String): Call<CreateAccountResponse> {
//        return apiService.createAccount(email, name)
//    }
}