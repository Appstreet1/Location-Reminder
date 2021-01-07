package com.udacity.project4.authentication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.udacity.project4.R
import com.udacity.project4.locationreminders.RemindersActivity
import kotlinx.android.synthetic.main.activity_authentication.*
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * This class should be the starting point of the app, It asks the users to sign in / register, and redirects the
 * signed in users to the RemindersActivity.
 */
class AuthenticationActivity : AppCompatActivity() {

    private val authenticationViewModel by viewModel<AuthenticationViewModel>()
    lateinit var providers: MutableList<AuthUI.IdpConfig>
    private val RC_SIGN_IN = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)
        setUpLoginProviders()

        initOnClick()
    }

    private fun setUpLoginProviders() {
        providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
    }

    private fun startLoginBuilder() {
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN
        )
    }

    override fun onStart() {
        super.onStart()
        checkIfUserLoggedIn()
    }

    private fun initOnClick() {
        auth_login.setOnClickListener {
            startLoginBuilder()
        }
    }

    private fun checkIfUserLoggedIn() {
        if (authenticationViewModel.currentUser.value != null) {
            navigateToRemindersActivity()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {

                navigateToRemindersActivity()

            } else {
                Log.i("TEST", response?.error.toString())
            }
        }
    }


    private fun navigateToRemindersActivity() {
        val intent = Intent(this@AuthenticationActivity, RemindersActivity::class.java)
        startActivity(intent)
        finish()
    }

}
