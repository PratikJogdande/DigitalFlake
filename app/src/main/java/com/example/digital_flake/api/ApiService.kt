package com.example.digital_flake.api

import com.example.digital_flake.model.AvailabilityResponse
import com.example.digital_flake.model.BookingConfirm
import com.example.digital_flake.model.BookingConfirmResponse
import com.example.digital_flake.model.BookingResponse
import com.example.digital_flake.model.CreateAccountResponse
import com.example.digital_flake.model.LoginRequest
import com.example.digital_flake.model.LoginResponse
import com.example.digital_flake.model.SlotResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("digitalflake/api/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST("digitalflake/api/create_account")
    suspend fun createAccount(
        @Field("email") email: String,
        @Field("name") name: String
    ): Response<CreateAccountResponse>

    @FormUrlEncoded
    @POST("digitalflake/api/confirm_booking")
    suspend fun createBookingConfirm(
        @Field("date") date: String,
        @Field("slot_id") slot_id: Int,
        @Field("workspace_id") workspace_id: Int,
        @Field("type") type: Int
    ): Response<BookingConfirmResponse>


    @GET("digitalflake/api/get_bookings")
    suspend fun getBookings(@Query("user_id") userId: Int): Response<BookingResponse>

    @GET("digitalflake/api/get_slots")
    suspend fun getSlots(@Query("date") date: String): Response<SlotResponse>

    @GET("digitalflake/api/get_availability")
    suspend fun getAvailability(@Query("date") date: String,
                                @Query("slot_id") selectedSlotId: Int,
                                @Query("type") type: Int): Response<AvailabilityResponse>

}