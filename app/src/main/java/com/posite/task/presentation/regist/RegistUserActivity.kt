package com.posite.task.presentation.regist

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Rect
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import com.google.android.material.textfield.TextInputEditText
import com.posite.task.R
import com.posite.task.databinding.ActivityRegistUserBinding
import com.posite.task.presentation.base.BaseActivity
import com.posite.task.presentation.regist.vm.RegistUserViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class RegistUserActivity :
    BaseActivity<RegistUserViewModelImpl, ActivityRegistUserBinding>(R.layout.activity_regist_user) {
    override val viewModel: RegistUserViewModelImpl by viewModels<RegistUserViewModelImpl>()

    override fun initView() {
        binding.birthdayEdit.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, { _, cYear, cMonth, cDay ->
                run {
                    binding.birthdayEdit.text =
                        getString(R.string.birthday_format, cYear, cMonth + 1, cDay)
                }
            }, year, month, day).show()
        }
    }

    override fun initObserver() {

    }


    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is TextInputEditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }


}