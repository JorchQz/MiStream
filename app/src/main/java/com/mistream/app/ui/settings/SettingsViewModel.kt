package com.mistream.app.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mistream.app.data.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val _saveSuccess = MutableStateFlow(false)
    val saveSuccess: StateFlow<Boolean> = _saveSuccess

    suspend fun getCurrentRdToken(): String = preferencesRepository.getRdToken()

    suspend fun getCurrentTmdbKey(): String = preferencesRepository.getTmdbKey()

    fun saveSettings(rdToken: String, tmdbKey: String) {
        viewModelScope.launch {
            if (rdToken.isNotEmpty()) preferencesRepository.saveRdToken(rdToken)
            if (tmdbKey.isNotEmpty()) preferencesRepository.saveTmdbKey(tmdbKey)
            _saveSuccess.value = true
        }
    }
}
