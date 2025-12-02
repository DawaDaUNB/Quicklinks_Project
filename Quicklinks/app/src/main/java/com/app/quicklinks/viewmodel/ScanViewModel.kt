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
    var AZ: Boolean = true
    var newest: Boolean = true


    fun loadHistory(uId: Long) {
        viewModelScope.launch {
            if(newest) {
                repo.getHistory(uId).collect { list ->
                    _history.value = list
                }
                newest = false;
            }
            else{
                repo.getHistoryReverse(uId).collect { list ->
                    _history.value = list
                }
                newest = true;
            }
        }
    }

    fun loadHistoryAlphabetical(uId: Long) {
        viewModelScope.launch {
            if(AZ) {
                repo.getHistoryAlphabetical(uId).collect { list ->
                    _history.value = list
                }
                AZ = false
            }
            else{
                repo.getHistoryAlphabeticalReserve(uId).collect { list ->
                    _history.value = list
                }
                AZ = true
            }
        }
    }

    fun searchHistory(substring: String, uId: Long) {
        viewModelScope.launch {
            repo.scanSearch(substring, uId).collect { list ->
                _history.value = list
            }
        }
    }

    fun saveScan(value: String, oURL: String, sURL: String, uId: Long) {
        viewModelScope.launch {
            repo.saveScan(value, oURL, sURL, uId)
            loadHistory(uId)
        }
    }

    fun deleteScan(scan: Scan, uId: Long) {
        viewModelScope.launch {
            repo.deleteScan(scan)
            newest = true;
            loadHistory(uId)
        }
    }

    fun updateName(scan: Scan,newName: String, uId: Long) {
        if(newName != "") {
            viewModelScope.launch {
                repo.updateScanName(scan.id, newName)
                newest = true
                loadHistory(uId)
            }
        }
    }

    fun updateFavorite(scan: Scan, favorite: Boolean, uId: Long) {
        viewModelScope.launch {
            repo.updateScanFavorite(scan.id,favorite)
            newest = true
            loadHistory(uId)
        }
    }


}