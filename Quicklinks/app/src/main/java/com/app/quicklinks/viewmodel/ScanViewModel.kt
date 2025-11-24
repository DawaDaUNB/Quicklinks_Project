package com.app.quicklinks.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.quicklinks.data.Scan
import com.app.quicklinks.data.ScanRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ScanViewModel(private val repo: ScanRepository) : ViewModel() {

    private val _history = MutableStateFlow<List<com.app.quicklinks.data.Scan>>(emptyList())
    val history: StateFlow<List<com.app.quicklinks.data.Scan>> = _history

    fun loadHistory() {
        viewModelScope.launch {
            repo.getHistory().collect { list ->
                _history.value = list
            }
        }
    }

    fun saveScan(value: String, oUrl: String, sUrl: String) {
        viewModelScope.launch {
            repo.saveScan(value, oUrl, sUrl)
            loadHistory()
        }
    }

    fun deleteScan(scan: Scan) {
        viewModelScope.launch {
            repo.deleteScan(scan)
            loadHistory()
        }
    }
}