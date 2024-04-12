package com.posite.task.presentation.regist.vm

import com.posite.task.presentation.regist.model.UserInfo
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface RegistUserViewModel {
    val userName: StateFlow<String>
    val userBirthday: StateFlow<String>
    val isRegisted: SharedFlow<Boolean>

    fun checkRegisted()

    fun saveUserInfo(userInfo: UserInfo)
}