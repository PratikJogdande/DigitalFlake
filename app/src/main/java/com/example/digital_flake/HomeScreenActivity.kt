package com.example.digital_flake

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class HomeScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)
        val bookingHistoryBtn = findViewById<Button>(R.id.bookingHistoryBtn)
        val squareItem1: LinearLayout = findViewById(R.id.squareItem1)
        val squareItem2: LinearLayout = findViewById(R.id.squareItem2)


        squareItem1.setOnClickListener {
            val intent = Intent(this, SelectDateActivity::class.java)
            startActivity(intent)
        }

        squareItem2.setOnClickListener {
            val intent = Intent(this, SelectDateActivity::class.java)
            startActivity(intent)
        }


        bookingHistoryBtn.setOnClickListener {
            val intent = Intent(this, BookingHistoryActivity::class.java)
            startActivity(intent)
        }
    }
}