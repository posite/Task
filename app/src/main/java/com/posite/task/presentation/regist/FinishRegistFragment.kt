package com.posite.task.presentation.regist

import android.content.Intent
import androidx.navigation.fragment.navArgs
import com.posite.task.R
import com.posite.task.databinding.FragmentFinishRegistBinding
import com.posite.task.presentation.base.BaseFragment
import com.posite.task.presentation.todo.TaskActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FinishRegistFragment :
    BaseFragment<FragmentFinishRegistBinding>(R.layout.fragment_finish_regist) {
    override fun initObserver() {

    }

    override fun initView() {
        binding.profileImage.clipToOutline = true
        val args: FinishRegistFragmentArgs by navArgs() //Args 만든 후
        val userInfo = args.userInfo // 아까 만든 msg 를 tMsg에 대입
        with(binding) {
            userName.text = userInfo.name
            userBirthday.text = userInfo.birthday
            profileImage.setImageBitmap(userInfo.profile)
            finishRegistBtn.setOnClickListener {
                val intent = Intent(requireContext(), TaskActivity::class.java)
                intent.putExtra("userInfo", userInfo)
                startActivity(intent)
                requireActivity().finish()
            }
        }
    }

    //fragment에서 뒤로가기 버튼 눌렀을 때

}