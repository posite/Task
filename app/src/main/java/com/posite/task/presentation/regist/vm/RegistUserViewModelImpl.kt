package com.posite.task.presentation.regist.vm

import androidx.lifecycle.viewModelScope
import com.posite.task.R
import com.posite.task.TaskApplication.Companion.getString
import com.posite.task.presentation.base.BaseViewModel
import com.posite.task.presentation.regist.model.UserInfo
import com.posite.task.util.BitmapConverter
import com.posite.task.util.DataStoreUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import javax.inject.Inject

@HiltViewModel
class RegistUserViewModelImpl @Inject constructor(private val dataStore: DataStoreUtil) :
    BaseViewModel(), RegistUserViewModel {
    val _userName: MutableStateFlow<String> = MutableStateFlow("")
    override val userName: StateFlow<String>
        get() = _userName

    val _userBirthday: MutableStateFlow<String> = MutableStateFlow("")
    override val userBirthday: StateFlow<String>
        get() = _userBirthday

    private val _isRegisted: MutableSharedFlow<Boolean> = MutableSharedFlow()
    override val isRegisted: SharedFlow<Boolean>
        get() = _isRegisted

    override fun checkRegisted() {
        viewModelScope.launch {
            if (dataStore.loadUserBirthday() != null && dataStore.loadUserProfile()
                    .isNotBlank() && dataStore.loadUserName().isNotBlank()
            ) {
                _isRegisted.emit(true)
            }
        }
    }

    override fun saveUserInfo(userInfo: UserInfo) {
        viewModelScope.launch {
            dataStore.saveUserName(userInfo.name)
            val date = SimpleDateFormat(getString(R.string.convert_format)).parse(
                userInfo.birthday
            )
            dataStore.saveUserBirthday(date!!)
            dataStore.saveUserProfile(BitmapConverter.bitmapToString(userInfo.profile!!))
        }
    }


}