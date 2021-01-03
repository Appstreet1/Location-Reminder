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

    enum class LoginStatus { Error, Success }

    val loginStatus: LiveData<LoginStatus>
        get() = _loginStatus

    val currentUser: LiveData<FirebaseUser>
        get() = _currentUser

    val lastSignedInGoogleAcc: LiveData<GoogleSignInAccount>
        get() = _lastSignedInGoogleAcc

    val googleSignInCient : LiveData<GoogleSignInClient>
        get() = _googleSignInClient

    private val _googleSignInClient = MutableLiveData<GoogleSignInClient>()
    private val _lastSignedInGoogleAcc = MutableLiveData<GoogleSignInAccount>()
    private val _loginStatus = MutableLiveData<LoginStatus>()
    private val _currentUser = MutableLiveData<FirebaseUser>()

    init {
        getCurrentUser()
        getLastSignedInGoogleAcc()
    }


     fun initGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        _googleSignInClient.value = GoogleSignIn.getClient(app.applicationContext, gso);
    }

    private fun getLastSignedInGoogleAcc() {
        val account = GoogleSignIn.getLastSignedInAccount(app.applicationContext)

        _lastSignedInGoogleAcc.value = account
    }

    fun signInEmail(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.IO) {
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    try {
                        firebaseAuth.signInWithEmailAndPassword(email, password)
                        _loginStatus.postValue(LoginStatus.Success)
                    } catch (e: Exception) {
                        _loginStatus.postValue(LoginStatus.Error)
                        Log.i("TEST", e.toString())
                    }
                } else {
                    _loginStatus.postValue(LoginStatus.Error)
                }
            }
        }
    }

    private fun getCurrentUser() {
        _currentUser.value = firebaseAuth.currentUser
    }
}
