package com.app.quicklinks.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.quicklinks.data.user.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(private val repository: UserRepository) : ViewModel() {

    fun registerUser(email: String, username: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val exists = repository.doesUserExist(email)

            if (exists) {
                onResult(false)
            } else {
                repository.createUser(email, username, password)
                onResult(true)
            }
        }
    }

    fun loginUser(email: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = repository.loginUser(email, password)
            onResult(success)
        }
    }

    fun resetPassword(email: String, newPassword: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = repository.updatePassword(email, newPassword)
            onResult(success)
        }
    }
}
