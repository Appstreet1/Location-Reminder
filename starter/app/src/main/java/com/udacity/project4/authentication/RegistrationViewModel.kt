package com.udacity.project4.authentication

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegistrationViewModel(private val firebaseAuth: FirebaseAuth) : ViewModel() {

    enum class RegistrationStatus { Error, Success }

    val registrationStatus: LiveData<RegistrationStatus>
        get() = _registrationStatus

    private val _registrationStatus = MutableLiveData<RegistrationStatus>()

    fun createUser(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.IO) {
                if(email.isNotEmpty() && password.isNotEmpty()){
                    try {
                        firebaseAuth.createUserWithEmailAndPassword(email, password)
                        _registrationStatus.postValue(RegistrationStatus.Success)
                    } catch (e: Exception) {
                        _registrationStatus.postValue(RegistrationStatus.Error)
                        Log.i("TEST", e.toString())
                    }
                }else{
                    _registrationStatus.postValue(RegistrationStatus.Error)
                }
            }
        }
    }
}