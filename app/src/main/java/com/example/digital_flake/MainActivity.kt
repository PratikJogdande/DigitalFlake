package com.example.digital_flake

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.example.digital_flake.network.RetrofitInstance
import com.example.digital_flake.repository.UserRepository
import com.example.digital_flake.viewModel.LoginModelViewFactory
import com.example.digital_flake.viewModel.LoginViewModel

class MainActivity : AppCompatActivity() {


    private lateinit var loginViewModel: LoginViewModel
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var textSignUp: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        emailEditText = findViewById(R.id.email_edtxt)
        passwordEditText = findViewById(R.id.password_edtxt)
        loginButton = findViewById(R.id.login_button)
        textSignUp = findViewById(R.id.textSignUp)


        textSignUp.setOnClickListener {
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
        }

    loginViewModel = ViewModelProvider(
        this,
        LoginModelViewFactory(UserRepository(RetrofitInstance.create(),this))
    ).get(LoginViewModel::class.java)

    // Set up button click listener
    loginButton.setOnClickListener {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        loginViewModel.login(email, password)
    }

    // Observe login result
    loginViewModel.loginResult.observe(this, Observer { result ->
        result.onSuccess { response ->
            Toast.makeText(this, "Success: ${response.message}", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, HomeScreenActivity::class.java)
            startActivity(intent)
            finish()

        }.onFailure { error ->
            Toast.makeText(this, "Login Failed: ${error.localizedMessage}", Toast.LENGTH_SHORT).show()
            Log.e(TAG, "Failure: "+error)
        }
    })
}
}