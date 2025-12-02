package com.app.quicklinks.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.quicklinks.data.ScanRepository

class ScanViewModelFactory(
    private val repo: ScanRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScanViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ScanViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}