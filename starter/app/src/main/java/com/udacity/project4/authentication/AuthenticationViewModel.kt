package com.udacity.project4.authentication

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthenticationViewModel(
    private val firebaseAuth: FirebaseAuth,
    private val app: Application
) : ViewModel() {

    val currentUser: LiveData<FirebaseUser>
        get() = _currentUser

    private val _lastSignedInGoogleAcc = MutableLiveData<GoogleSignInAccount>()
    private val _currentUser = MutableLiveData<FirebaseUser>()

    init {
        getCurrentUser()
        getLastSignedInGoogleAcc()
    }

    private fun getLastSignedInGoogleAcc() {
        val account = GoogleSignIn.getLastSignedInAccount(app.applicationContext)

        _lastSignedInGoogleAcc.value = account
    }

    private fun getCurrentUser() {
        _currentUser.value = firebaseAuth.currentUser
    }
}
