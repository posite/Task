package com.posite.task.presentation.regist.vm

import com.posite.task.presentation.regist.model.UserInfo
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface RegistUserViewModel {
    val userName: StateFlow<String>
    val userBirthday: StateFlow<String>
    val finishRegist: SharedFlow<Boolean>
    val saveFinshed: SharedFlow<Boolean>

    fun saveUserInfo(userInfo: UserInfo)
    fun registUser()
}