package com.app.quicklinks.viewmodel
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class LoginAuth : ViewModel()  {
    // The current logged-in user
    var userId by mutableStateOf<Long?>(-1L)
        private set

    fun login(id: Long) {
        userId = id
    }

    fun logout() {
        userId = -1L
    }
}