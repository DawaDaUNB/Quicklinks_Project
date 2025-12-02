package com.app.quicklinks.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow

class LoginAuth {
    // The current logged-in user
    val userId = MutableStateFlow<Long?>(null)

    fun setUser(id: Long) {
        userId.value = id
    }

    fun logout() {
        userId.value = -1
    }
}