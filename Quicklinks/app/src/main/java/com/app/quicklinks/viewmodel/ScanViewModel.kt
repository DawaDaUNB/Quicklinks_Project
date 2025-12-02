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

    private var uId: Long = -1


    fun loadHistory() {
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

    fun loadHistoryAlphabetical() {
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

    fun searchHistory(substring: String) {
        viewModelScope.launch {
            repo.scanSearch(substring, uId).collect { list ->
                _history.value = list
            }
        }
    }

    fun saveScan(value: String, oURL: String, sURL: String) {
        viewModelScope.launch {
            repo.saveScan(value, oURL, sURL, uId)
            loadHistory()
        }
    }

    fun deleteScan(scan: Scan) {
        viewModelScope.launch {
            repo.deleteScan(scan)
            newest = true;
            //loadHistory()
        }
    }

    fun updateName(scan: Scan,newName: String) {
        if(newName != "") {
            viewModelScope.launch {
                repo.updateScanName(scan.id, newName)
                //loadHistory()
                newest = true;
            }
        }
    }

    fun updateFavorite(scan: Scan, favorite: Boolean) {
        viewModelScope.launch {
            repo.updateScanFavorite(scan.id,favorite)
        }
    }

    fun changeUser(userId: Long) {
        uId = userId
    }

}