package com.udacity.project4.authentication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.udacity.project4.R
import kotlinx.android.synthetic.main.activity_authentication.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * This class should be the starting point of the app, It asks the users to sign in / register, and redirects the
 * signed in users to the RemindersActivity.
 */
class AuthenticationActivity : AppCompatActivity() {

    private val authenticationActivity by viewModel<AuthenticationViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        auth_signin_email.setOnClickListener {

        }

        auth_create_acc.setOnClickListener {
            val intent = Intent(this@AuthenticationActivity, RegistrationActvity::class.java)
            startActivity(intent)
        }

//          TODO: If the user was authenticated, send him to RemindersActivity

        //TODO : If the user has no account, send him to RegisterActivity

//          TODO: a bonus is to customize the sign in flow to look nice using :
        //https://github.com/firebase/FirebaseUI-Android/blob/master/auth/README.md#custom-layout

    }
}
