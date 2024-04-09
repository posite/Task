package com.posite.task.presentation.regist.vm

import androidx.lifecycle.viewModelScope
import com.posite.task.presentation.base.BaseViewModel
import com.posite.task.presentation.model.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistUserViewModelImpl @Inject constructor() : BaseViewModel(), RegistUserViewModel {
    val _userName: MutableStateFlow<String> = MutableStateFlow("")
    override val userName: StateFlow<String>
        get() = _userName

    val _userBirthday: MutableStateFlow<String> = MutableStateFlow("")
    override val userBirthday: StateFlow<String>
        get() = _userBirthday

    private val _userInfo: MutableStateFlow<UserInfo> = MutableStateFlow(UserInfo("", ""))
    override val userInfo: StateFlow<UserInfo>
        get() = _userInfo

    override fun registBtnClick() {
        viewModelScope.launch {
            _userInfo.emit(UserInfo(_userName.value, _userBirthday.value))
        }
    }
}