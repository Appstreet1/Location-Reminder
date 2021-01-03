package com.udacity.project4.authentication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.udacity.project4.R
import com.udacity.project4.locationreminders.RemindersActivity
import kotlinx.android.synthetic.main.activity_authentication.*
import kotlinx.android.synthetic.main.activity_registration.*
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * This class should be the starting point of the app, It asks the users to sign in / register, and redirects the
 * signed in users to the RemindersActivity.
 */
class AuthenticationActivity : AppCompatActivity() {

    private val authenticationViewModel by viewModel<AuthenticationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        initOnClick()
        observeLoginStatus()
        observerGoogleLogin()
    }

    override fun onStart() {
        super.onStart()
        checkIfUserLoggedIn()
    }

    private fun initOnClick() {
        auth_signin_email.setOnClickListener {
            val email = auth_email.text
            val password = auth_password.text

            authenticationViewModel.signInEmail(email.toString(), password.toString())
        }

        auth_create_acc.setOnClickListener {
            val intent = Intent(this@AuthenticationActivity, RegistrationActvity::class.java)
            startActivity(intent)
        }

        auth_signin_google.setOnClickListener {
            startGoogleActivity()
        }
    }

    private fun checkIfUserLoggedIn() {
        if (authenticationViewModel.currentUser.value?.email != null) {
            navigateToRemindersActivity()
        }

        if (authenticationViewModel.lastSignedInGoogleAcc.value != null) {
            navigateToRemindersActivity()
        }
    }

    private fun startGoogleActivity() {
        authenticationViewModel.initGoogleSignIn()
    }

    private fun observerGoogleLogin() {
        authenticationViewModel.googleSignInCient.observe(this, Observer { googleSignInClient ->
            startActivityForResult(googleSignInClient.signInIntent, 1)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {

            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            navigateToRemindersActivity()
        }
    }

    private fun observeLoginStatus() {
        authenticationViewModel.loginStatus.observe(this, Observer { loginStatus ->
            when (loginStatus) {
                AuthenticationViewModel.LoginStatus.Success -> navigateToRemindersActivity()
                AuthenticationViewModel.LoginStatus.Error -> showError()
            }
        })
    }

    private fun showError() {
        Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
    }

    private fun navigateToRemindersActivity() {
        val intent = Intent(this@AuthenticationActivity, RemindersActivity::class.java)
        startActivity(intent)
        finish()
    }
}
