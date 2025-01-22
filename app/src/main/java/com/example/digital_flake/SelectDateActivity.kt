package com.example.digital_flake

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.digital_flake.adapter.BookingAdapter
import com.example.digital_flake.adapter.CalendarAdapter
import com.example.digital_flake.adapter.SlotAdapter
import com.example.digital_flake.databinding.ActivityBookingHistoryBinding
import com.example.digital_flake.databinding.ActivitySelectDateBinding
import com.example.digital_flake.model.CalendarDateModel
import com.example.digital_flake.network.RetrofitInstance
import com.example.digital_flake.repository.UserRepository
import com.example.digital_flake.utils.HorizontalItemDecoration
import com.example.digital_flake.viewModel.BookingViewModel
import com.example.digital_flake.viewModel.BookingViewModelFactory
import com.example.digital_flake.viewModel.SlotViewModel
import com.example.digital_flake.viewModel.SlotViewModelFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class SelectDateActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelectDateBinding
    private val sdf = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)
    private val cal = Calendar.getInstance(Locale.ENGLISH)
    private val currentDate = Calendar.getInstance(Locale.ENGLISH)
    private val dates = ArrayList<Date>()
    private lateinit var adapter: CalendarAdapter
    private val calendarList2 = ArrayList<CalendarDateModel>()
    private val slotViewModel: SlotViewModel by viewModels{
        SlotViewModelFactory(repository)
    }
    private lateinit var slotAdapter: SlotAdapter

    private val apiService = RetrofitInstance.create()

    private val repository = UserRepository(apiService,this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySelectDateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpAdapter()
        setUpClickListener()
        setUpCalendar()


        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        binding.nextBtn.setOnClickListener {
            // Start the new Activity
            val intent = Intent(this, AvailableDesksActivity::class.java)  // Replace NewActivity with your target activity
            startActivity(intent)
        }
    }


private fun setUpClickListener() {
    binding.ivCalendarNext.setOnClickListener {
        cal.add(Calendar.MONTH, 1)
        setUpCalendar()
    }
    binding.ivCalendarPrevious.setOnClickListener {
        cal.add(Calendar.MONTH, -1)
        if (cal == currentDate)
            setUpCalendar()
        else
            setUpCalendar()
    }
}

private fun setUpAdapter() {
    val spacingInPixels = resources.getDimensionPixelSize(R.dimen.single_calendar_margin)
    binding.recyclerView.addItemDecoration(HorizontalItemDecoration(spacingInPixels))
    val snapHelper: SnapHelper = LinearSnapHelper()
    snapHelper.attachToRecyclerView(binding.recyclerView)
    adapter = CalendarAdapter { calendarDateModel: CalendarDateModel, position: Int ->
        calendarList2.forEachIndexed { index, calendarModel ->
            calendarModel.isSelected = index == position
            Log.i(TAG, "setUpAdapter: "+calendarDateModel.formattedDate);
            callApi(calendarDateModel.formattedDate);
        }
        adapter.setData(calendarList2)
    }
    binding.recyclerView.adapter = adapter
}

    private fun callApi(formattedDate: String) {
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        // Save the formattedDate in SharedPreferences
        sharedPreferences.edit().apply {
            putString("formatted_date", formattedDate)  // Store the formatted date
            apply()  // Commit the changes asynchronously
        }
        val recyclerView: RecyclerView = findViewById(R.id.timeSlotRV)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        slotAdapter = SlotAdapter(emptyList(), this)  // Initialize with empty list
        recyclerView.adapter = slotAdapter

        // Observe LiveData from ViewModel
        slotViewModel.slots.observe(this, Observer { bookings ->
            bookings?.let {
                slotAdapter.updateSlots(it)
            }
        })

        slotViewModel.fetchSlots(formattedDate)



    }

    /**
 * Function to setup calendar for every month
 */
private fun setUpCalendar() {
    val calendarList = ArrayList<CalendarDateModel>()
    binding.tvDateMonth.text = sdf.format(cal.time)
    val monthCalendar = cal.clone() as Calendar
    val maxDaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
    dates.clear()
    monthCalendar.set(Calendar.DAY_OF_MONTH, 1)
    while (dates.size < maxDaysInMonth) {
        dates.add(monthCalendar.time)
        calendarList.add(CalendarDateModel(monthCalendar.time))
        monthCalendar.add(Calendar.DAY_OF_MONTH, 1)
    }
    calendarList2.clear()
    calendarList2.addAll(calendarList)
    adapter.setData(calendarList)
}

}