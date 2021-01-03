package com.udacity.project4.authentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.udacity.project4.R
import kotlinx.android.synthetic.main.activity_registration.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegistrationActvity : AppCompatActivity() {

    private val registrationViewModel by viewModel<RegistrationViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        initOnClick()
        observeRegistrationStatus()
    }

    private fun observeRegistrationStatus() {
        registrationViewModel.registrationStatus.observe(this, Observer { status ->
            when (status) {
                RegistrationViewModel.RegistrationStatus.Success ->
                    Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                RegistrationViewModel.RegistrationStatus.Error ->
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun initOnClick() {
        reg_signup_email.setOnClickListener {
            val email = reg_email.text
            val password = reg_password.text

            registrationViewModel.createUser(email.toString(), password.toString())
        }
    }
}