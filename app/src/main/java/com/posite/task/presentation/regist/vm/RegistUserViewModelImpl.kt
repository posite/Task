package com.posite.task.presentation.regist.vm

import com.posite.task.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class RegistUserViewModelImpl @Inject constructor() : BaseViewModel(), RegistUserViewModel {
    val _userName: MutableStateFlow<String> = MutableStateFlow("")
    override val userName: StateFlow<String>
        get() = _userName

    val _userBirthday: MutableStateFlow<String> = MutableStateFlow("")
    override val userBirthday: StateFlow<String>
        get() = _userBirthday


}