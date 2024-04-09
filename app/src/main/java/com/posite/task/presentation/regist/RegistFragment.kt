package com.posite.task.presentation.regist

import android.app.DatePickerDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import com.posite.task.R
import com.posite.task.databinding.FragmentRegistBinding
import com.posite.task.presentation.base.BaseFragment
import com.posite.task.presentation.regist.vm.RegistUserViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Calendar

@AndroidEntryPoint
class RegistFragment :
    BaseFragment<FragmentRegistBinding>(R.layout.fragment_regist) {
    private val viewModel: RegistUserViewModelImpl by viewModels<RegistUserViewModelImpl>()
    private var calendar = Calendar.getInstance()
    private var year = calendar.get(Calendar.YEAR)
    private var month = calendar.get(Calendar.MONTH)
    private var day = calendar.get(Calendar.DAY_OF_MONTH)
    override fun initObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userInfo.collect {
                    if (it.name.isNotEmpty() && it.birthday.isNotEmpty()) {
                        val action =
                            RegistFragmentDirections.actionRegistFragmentToFinishRegistFragment(it)
                        findNavController(this@RegistFragment).navigate(action)
                    }
                }
            }
        }
    }

    override fun initView() {
        binding.viewModel = viewModel
        binding.birthdayEdit.setOnClickListener {
            val dialog = DatePickerDialog(
                requireContext(),
                R.style.MySpinnerDatePickerStyle,
                { _, cYear, cMonth, cDay ->
                    run {
                        if (cMonth < 10 && cDay < 10) {
                            binding.birthdayEdit.text =
                                getString(
                                    R.string.birthday_format,
                                    cYear,
                                    "0${cMonth + 1}",
                                    "0$cDay"
                                )
                        } else if (cMonth < 10) {
                            binding.birthdayEdit.text =
                                getString(
                                    R.string.birthday_format,
                                    cYear,
                                    "0${cMonth + 1}",
                                    cDay.toString()
                                )
                        } else if (cDay < 10) {
                            binding.birthdayEdit.text =
                                getString(
                                    R.string.birthday_format,
                                    cYear,
                                    (cMonth + 1).toString(),
                                    "0$cDay"
                                )
                        } else {
                            binding.birthdayEdit.text =
                                getString(
                                    R.string.birthday_format,
                                    cYear,
                                    (cMonth + 1).toString(),
                                    cDay.toString()
                                )
                        }
                        year = cYear
                        month = cMonth
                        day = cDay
                    }
                },
                year,
                month,
                day
            )
            val color = ContextCompat.getColor(requireActivity(), R.color.highlight)
            dialog.show()
            dialog.getButton(DatePickerDialog.BUTTON_POSITIVE)
                .setTextColor(color)
            dialog.getButton(DatePickerDialog.BUTTON_NEGATIVE)
                .setTextColor(color)
        }
    }

}