package com.app.quicklinks.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.quicklinks.data.Scan
import com.app.quicklinks.data.ScanRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ScanViewModel(private val repo: ScanRepository) : ViewModel() {

    private val _history = MutableStateFlow<List<Scan>>(emptyList())
    val history: StateFlow<List<Scan>> = _history

    fun loadHistory() {
        viewModelScope.launch {
            repo.getHistory().collect { list ->
                _history.value = list
            }
        }
    }

    fun loadHistoryAlphabetical() {
        viewModelScope.launch {
            repo.getHistoryAlphabetical().collect { list ->
                _history.value = list
            }
        }
    }

    fun saveScan(value: String, oURL: String, sURL: String) {
        viewModelScope.launch {
            repo.saveScan(value, oURL, sURL)
            loadHistory()
        }
    }

    fun deleteScan(scan: Scan) {
        viewModelScope.launch {
            repo.deleteScan(scan)
            loadHistory()
        }
    }

    fun updateName(scan: Scan,newName: String) {
        viewModelScope.launch {
            repo.updateScanName(scan.id,newName)
            loadHistory()
        }
    }

    fun updateFavorite(scan: Scan, favorite: Boolean) {
        viewModelScope.launch {
            repo.updateScanFavorite(scan.id,favorite)
            loadHistory()
        }
    }
}