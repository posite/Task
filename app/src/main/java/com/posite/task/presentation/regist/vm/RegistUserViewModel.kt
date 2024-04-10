package com.posite.task.presentation.regist.vm

import kotlinx.coroutines.flow.StateFlow

interface RegistUserViewModel {
    val userName: StateFlow<String>
    val userBirthday: StateFlow<String>
}