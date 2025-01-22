package com.example.digital_flake

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.digital_flake.network.RetrofitInstance
import com.example.digital_flake.repository.UserRepository
import com.example.digital_flake.viewModel.CreateAccountViewModel
import com.example.digital_flake.viewModel.CreateAccountViewModelFactory
import androidx.lifecycle.Observer

class CreateAccountActivity : AppCompatActivity() {

    private lateinit var createAccountViewModel: CreateAccountViewModel
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var createAccountButton: Button
    private lateinit var createAccountTextView : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_create_account)


        emailEditText = findViewById(R.id.emailEdTxt)
        nameEditText = findViewById(R.id.fullNameEdTxt)
        createAccountButton = findViewById(R.id.submitBtn)

        createAccountTextView = findViewById(R.id.textExistingUser)
        createAccountTextView.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        createAccountViewModel = ViewModelProvider(
            this,
            CreateAccountViewModelFactory(UserRepository(RetrofitInstance.create(),this))
        ).get(CreateAccountViewModel::class.java)

        createAccountButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = nameEditText.text.toString()

            createAccountViewModel.createAccount(email, password)
        }

        // Observe login result
        createAccountViewModel.createAccResult.observe(this, Observer { result ->
            result.onSuccess { response ->
                Toast.makeText(this, "Success: ${response.message}", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()

            }.onFailure { error ->
                Toast.makeText(this, "Login Failed: ${error.localizedMessage}", Toast.LENGTH_SHORT).show()
                Log.e(ContentValues.TAG, "Failure: "+error)
            }
        })
    }
}