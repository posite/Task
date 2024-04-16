package com.posite.task.presentation.splash.vm

import kotlinx.coroutines.flow.SharedFlow

interface SplashViewModel {
    val isRegisted: SharedFlow<Boolean>
    fun checkRegist()
}