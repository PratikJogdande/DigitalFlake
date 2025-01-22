package com.example.digital_flake

import android.app.AlertDialog

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log

import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.digital_flake.adapter.AvailabilityAdapter
import com.example.digital_flake.adapter.SlotAdapter
import com.example.digital_flake.databinding.ActivityAvailableDesksBinding
import com.example.digital_flake.model.BookingConfirmViewModelFactory
import com.example.digital_flake.model.DateFormat
import com.example.digital_flake.model.DateManager
import com.example.digital_flake.network.RetrofitInstance
import com.example.digital_flake.repository.UserRepository
import com.example.digital_flake.viewModel.AvailabilityViewModel
import com.example.digital_flake.viewModel.AvailabilityViewModelFactory
import com.example.digital_flake.viewModel.BookingConfirmViewModel

class AvailableDesksActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAvailableDesksBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var selectedPosition: Int = -1
    private var selectedSlotName: String? = null
    private var selectedSlotId: Int = -1
    private val apiService = RetrofitInstance.create()
    private lateinit var createAccountViewModel: BookingConfirmViewModel

    private val repository = UserRepository(apiService,this)

    private lateinit var slotAdapter: AvailabilityAdapter
    private val slotViewModel: AvailabilityViewModel by viewModels{
        AvailabilityViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityAvailableDesksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            onBackPressed()
        }



        val nextButton: Button = findViewById(R.id.nextBtn)
        nextButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                // workspace_id
                sharedPreferences = getSharedPreferences("AvailabilityPrefs", Context.MODE_PRIVATE)
                val workspaceId = sharedPreferences.getInt("selected_workspace_id", -1)  // selected workd id = 8// Default to -1 if not found
              //  val workspaceName = sharedPreferences.getString("selected_workspace_name", "Default Workspace") //workname = 8

                // slot_id
                sharedPreferences = getSharedPreferences("SlotPrefs", Context.MODE_PRIVATE)
                selectedSlotId = sharedPreferences.getInt("selected_slot_id", -1) //  slot_id

                // user_type
                sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                val userId = sharedPreferences.getInt("user_id", -1) //userid =12

                //date
                val retrievedDate = DateFormat.dateFormatIntoNumber

                showCustomDialog()

            }
        })



        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("SlotPrefs", Context.MODE_PRIVATE)

        // Retrieve the selected slot name from SharedPreferences
        selectedSlotId = sharedPreferences.getInt("selected_slot_id", -1)  //selected slot Id = 2 // Default is null if not found
        selectedSlotName = sharedPreferences.getString("selected_slot_name", null) // 11:00AM - 12:00PM // Default is null if not found

        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("user_id", -1)
        val date = sharedPreferences.getString("formatted_date", null)
        availableDesk(date,selectedSlotId, userId, selectedSlotName)

        Log.i("AvailableDesksActivity", "Selected Slot Name: $selectedSlotName")

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun availableDesk(date: String?, selectedSlotId: Int, userId: Int,Selected_Slot_Time: String?) {
        val recyclerView: RecyclerView = findViewById(R.id.deskSlotRV)
        recyclerView.layoutManager = GridLayoutManager(this, 5)

        slotAdapter = AvailabilityAdapter(emptyList(), this)
        recyclerView.adapter = slotAdapter

        // Observe LiveData from ViewModel
        slotViewModel.availability.observe(this, Observer { bookings ->
            bookings?.let {
                slotAdapter.updateSlots(it)
            }
        })

        slotViewModel.fetchAvailability(date.toString(), selectedSlotId, userId)

    }

    private fun showCustomDialog() {
        val dialogView = layoutInflater.inflate(R.layout.confirm_booking_dialog_box, null)
        sharedPreferences = getSharedPreferences("AvailabilityPrefs", Context.MODE_PRIVATE)

        val workspaceId = sharedPreferences.getInt("selected_workspace_id", -1)  // workId =1
        val workspaceName = sharedPreferences.getString("selected_workspace_name", "Default Workspace")

        sharedPreferences = getSharedPreferences("SlotPrefs", Context.MODE_PRIVATE)

        selectedSlotId = sharedPreferences.getInt("selected_slot_id", -1)  //selected slot Id = 2 // Default is null if not found
        selectedSlotName = sharedPreferences.getString("selected_slot_name", null) // 11:00AM - 12:00PM

        val savedFormattedDate = DateManager.currentDate.formattedDate
        println(savedFormattedDate)

        val time = selectedSlotName


        val tvDeskId = dialogView.findViewById<TextView>(R.id.tvDeskId)
        val tvDeskNumber = dialogView.findViewById<TextView>(R.id.tvDeskNumber)
        val tvSlot = dialogView.findViewById<TextView>(R.id.tvSlot)

        tvDeskId.text = "Desk ID: $workspaceId"
        tvDeskNumber.text = "Desk: $workspaceName"
        tvSlot.text = "Slot: $savedFormattedDate, $time"


        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        // Confirm button action
        val btnConfirm: View = dialogView.findViewById(R.id.btnConfirm)
        btnConfirm.setOnClickListener {

            sharedPreferences = getSharedPreferences("AvailabilityPrefs", Context.MODE_PRIVATE)
            val workspaceId = sharedPreferences.getInt("selected_workspace_id", -1)
            //  val workspaceName = sharedPreferences.getString("selected_workspace_name", "Default Workspace") //workname = 8

            // slot_id
            sharedPreferences = getSharedPreferences("SlotPrefs", Context.MODE_PRIVATE)
            selectedSlotId = sharedPreferences.getInt("selected_slot_id", -1) //  slot_id

            // user_type
            sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            val userId = sharedPreferences.getInt("user_id", -1) //userid =12

            //date
            val retrievedDate = DateFormat.dateFormatIntoNumber
            Log.i(TAG, "retrievedDate: "+retrievedDate+" selectedSlotId: "+selectedSlotId+" workspaceId: "+workspaceId+" workspaceId: "+userId)
            confirmBookApi(retrievedDate, selectedSlotId, workspaceId, userId)
            Toast.makeText(this@AvailableDesksActivity, "Congratulations your Booking is Confirm", Toast.LENGTH_SHORT).show()
        //    AlertCall()
            dialog.dismiss()
        }
        // Cancel button action
        val btnCancel: View = dialogView.findViewById(R.id.btnCancel)
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun confirmBookApi(retrievedDate: String?, selectedSlotId: Int, workspaceId: Int, userId: Int) {
        createAccountViewModel = ViewModelProvider(
            this,
            BookingConfirmViewModelFactory(UserRepository(RetrofitInstance.create(),this))
        ).get(BookingConfirmViewModel::class.java)
        createAccountViewModel.createAccount(retrievedDate.toString(), selectedSlotId,workspaceId,userId)
        // Observe the LiveData from the ViewModel for the booking response
        createAccountViewModel.createAccResult.observe(this, Observer { response ->
            // Check if the response is not null and is successful
            if (response != null) {
                val intent = Intent(this, HomeScreenActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Failed to book workspace", Toast.LENGTH_LONG).show()
            }
        })
    }
}