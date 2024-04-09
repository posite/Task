package com.posite.task.presentation.regist.vm

import com.posite.task.presentation.model.UserInfo
import kotlinx.coroutines.flow.StateFlow

interface RegistUserViewModel {
    val userName: StateFlow<String>
    val userBirthday: StateFlow<String>
    val userInfo: StateFlow<UserInfo>
    fun registBtnClick()
}