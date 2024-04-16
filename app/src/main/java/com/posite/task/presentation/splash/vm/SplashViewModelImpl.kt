package com.posite.task.presentation.splash.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.posite.task.util.DataStoreUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModelImpl @Inject constructor(private val dataStore: DataStoreUtil) : ViewModel(),
    SplashViewModel {
    private val _isRegisted = MutableSharedFlow<Boolean>()
    override val isRegisted: SharedFlow<Boolean>
        get() = _isRegisted

    override fun checkRegist() {
        viewModelScope.launch {
            Log.d("check", dataStore.loadUserName())
            Log.d("check", dataStore.loadUserProfile())
            Log.d("check", dataStore.loadUserBirthday())
            if (dataStore.loadUserProfile().isNotBlank() && dataStore.loadUserName()
                    .isNotBlank() && dataStore.loadUserBirthday().isNotBlank()
            ) {
                _isRegisted.emit(true)
            } else {
                _isRegisted.emit(false)
            }
        }
    }

}