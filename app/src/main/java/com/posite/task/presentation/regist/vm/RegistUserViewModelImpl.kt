package com.posite.task.presentation.regist.vm

import androidx.lifecycle.viewModelScope
import com.posite.task.presentation.base.BaseViewModel
import com.posite.task.presentation.regist.model.UserInfo
import com.posite.task.util.BitmapConverter
import com.posite.task.util.DataStoreUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
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

    private val _finishRegist: MutableSharedFlow<Boolean> = MutableSharedFlow()
    override val finishRegist: SharedFlow<Boolean>
        get() = _finishRegist

    private val _saveFinished: MutableSharedFlow<Boolean> = MutableSharedFlow()
    override val saveFinshed: SharedFlow<Boolean>
        get() = _saveFinished

    override fun saveUserInfo(userInfo: UserInfo) {
        viewModelScope.launch {
            viewModelScope.async {
                dataStore.saveUserProfile(BitmapConverter.bitmapToString(userInfo.profile!!))
                dataStore.saveUserName(userInfo.name)
                dataStore.saveUserBirthday(userInfo.birthday)
            }.await()
            _saveFinished.emit(true)
        }
    }

    override fun registUser() {
        viewModelScope.launch {
            _finishRegist.emit(true)
        }
    }


}