package com.posite.task.presentation.regist

import android.content.Intent
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.posite.task.R
import com.posite.task.databinding.FragmentFinishRegistBinding
import com.posite.task.presentation.base.BaseFragment
import com.posite.task.presentation.regist.model.UserInfo
import com.posite.task.presentation.regist.vm.RegistUserViewModelImpl
import com.posite.task.presentation.todo.TaskActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FinishRegistFragment :
    BaseFragment<FragmentFinishRegistBinding>(R.layout.fragment_finish_regist) {
    private val finishViewModel by viewModels<RegistUserViewModelImpl>()
    private lateinit var userInfo: UserInfo
    override fun initObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                finishViewModel.finishRegist.collect {
                    if (it) {
                        finishViewModel.saveUserInfo(userInfo)
                    }
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                finishViewModel.saveFinshed.collect {
                    if (it) {
                        val intent = Intent(requireContext(), TaskActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    }
                }
            }
        }
    }

    override fun initView() {
        binding.viewModel = finishViewModel
        binding.profileImage.clipToOutline = true
        val args: FinishRegistFragmentArgs by navArgs() //Args 만든 후
        userInfo = args.userInfo // 아까 만든 msg 를 tMsg에 대입
        with(binding) {
            userName.text = userInfo.name
            userBirthday.text = userInfo.birthday
            profileImage.setImageBitmap(userInfo.profile)

        }
    }

}