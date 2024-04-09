package com.posite.task.presentation.regist

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.posite.task.R
import com.posite.task.databinding.FragmentFinishRegistBinding
import com.posite.task.presentation.base.BaseFragment
import com.posite.task.presentation.regist.vm.RegistUserViewModel
import com.posite.task.presentation.regist.vm.RegistUserViewModelImpl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FinishRegistFragment :
    BaseFragment<FragmentFinishRegistBinding>(R.layout.fragment_finish_regist) {
    private val viewModel: RegistUserViewModel by viewModels<RegistUserViewModelImpl>()

    override fun initObserver() {

    }

    override fun initView() {
        val args: FinishRegistFragmentArgs by navArgs() //Args 만든 후
        val userInfo = args.userInfo // 아까 만든 msg 를 tMsg에 대입
        with(binding) {
            userName.text = userInfo.name
            userBirthday.text = userInfo.birthday
        }
    }

}