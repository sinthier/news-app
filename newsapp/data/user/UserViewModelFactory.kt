package com.loc.newsapp.data.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.loc.newsapp.data.user.UserDatabaseHelper
import com.loc.newsapp.data.user.UserViewModel

class UserViewModelFactory(private val databaseHelper: UserDatabaseHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(databaseHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
