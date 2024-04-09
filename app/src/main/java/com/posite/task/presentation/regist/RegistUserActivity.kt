package com.posite.task.presentation.regist

import androidx.activity.viewModels
import com.posite.task.R
import com.posite.task.databinding.ActivityRegistUserBinding
import com.posite.task.presentation.base.BaseActivity
import com.posite.task.presentation.regist.vm.RegistUserViewModelImpl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistUserActivity :
    BaseActivity<RegistUserViewModelImpl, ActivityRegistUserBinding>(R.layout.activity_regist_user) {
    override val viewModel: RegistUserViewModelImpl by viewModels<RegistUserViewModelImpl>()

    override fun initView() {

    }

    override fun initObserver() {

    }

}