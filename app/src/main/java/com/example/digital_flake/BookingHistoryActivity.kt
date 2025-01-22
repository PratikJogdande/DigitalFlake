package com.example.digital_flake

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.digital_flake.adapter.BookingAdapter
import com.example.digital_flake.databinding.ActivityBookingHistoryBinding
import com.example.digital_flake.model.Booking
import com.example.digital_flake.network.RetrofitInstance
import com.example.digital_flake.repository.UserRepository
import com.example.digital_flake.viewModel.BookingViewModel
import com.example.digital_flake.viewModel.BookingViewModelFactory

class BookingHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookingHistoryBinding
    private lateinit var bookingAdapter: BookingAdapter
    private val apiService = RetrofitInstance.create()
    private val repository = UserRepository(apiService,this)
    private val viewModel: BookingViewModel by viewModels {
        BookingViewModelFactory(repository)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_history)

        val recyclerView: RecyclerView = findViewById(R.id.rv_booking_history)
        recyclerView.layoutManager = LinearLayoutManager(this)
        bookingAdapter = BookingAdapter(emptyList())
        recyclerView.adapter = bookingAdapter

        viewModel.bookings.observe(this, Observer { bookings ->
            bookings?.let {
                bookingAdapter.updateBookings(it)
            }
        })

        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("user_id", -1)
        viewModel.fetchBookings(userId)

        val backArrow = findViewById<ImageView>(R.id.iv_back)
        backArrow.setOnClickListener {
            onBackPressed() // Navigate to the previous activity
        }
    }
}