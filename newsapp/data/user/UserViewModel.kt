package com.loc.newsapp.data.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loc.newsapp.data.user.UserDatabaseHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val databaseHelper: UserDatabaseHelper
) : ViewModel() {

    private val _registrationStatus = MutableLiveData<Boolean>()
    val registrationStatus: LiveData<Boolean> get() = _registrationStatus

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _loginStatus = MutableLiveData<Boolean>()
    val loginStatus: LiveData<Boolean> get() = _loginStatus

    fun registerUser(username: String, password: String) {
        val isRegistered = databaseHelper.registerUser(username, password)
        if (isRegistered) {
            _registrationStatus.postValue(true)
        } else {
            _errorMessage.postValue("Registration failed.")
        }
    }

    fun setError(message: String) {
        _errorMessage.value = message
    }

    fun loginUser(username: String, password: String) {
        val isLoggedIn = databaseHelper.loginUser(username, password)
        if (isLoggedIn) {
            _loginStatus.postValue(true)
        } else {
            _errorMessage.postValue("Invalid credentials.")
        }
    }
}

