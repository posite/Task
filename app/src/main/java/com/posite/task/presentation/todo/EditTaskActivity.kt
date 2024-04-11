package com.posite.task.presentation.todo

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.posite.task.R
import com.posite.task.TaskApplication
import com.posite.task.databinding.ActivityEditTaskBinding
import com.posite.task.presentation.base.BaseActivity
import com.posite.task.presentation.todo.model.UserTask
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class EditTaskActivity : BaseActivity<ActivityEditTaskBinding>(R.layout.activity_edit_task) {
    private var calendar = Calendar.getInstance()
    private var year = calendar.get(Calendar.YEAR)
    private var month = calendar.get(Calendar.MONTH)
    private var day = calendar.get(Calendar.DAY_OF_MONTH)
    override fun initObserver() {

    }

    override fun initView() {
        val task = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("userTask", UserTask::class.java)
        } else {
            intent.getParcelableExtra<UserTask>("userTask")
        }
        val type = intent.getStringExtra("edit_type")

        with(binding) {
            when (type) {
                "add" -> {
                    editTaskTitle.text = getString(R.string.add_task_title)
                    fishEditBtn.text = getString(R.string.add_btn)
                }

                "edit" -> {
                    editTaskTitle.text = getString(R.string.edit_task_title)
                    taskEdit.setText(task!!.taskTitle)
                    fishEditBtn.text = getString(R.string.edit_btn)
                    dateEdit.text = task.date.toString()
                }
            }
            fishEditBtn.setOnClickListener {
                val taskContent = taskEdit.text
                val taskDate = dateEdit.text
                if (taskContent.isNullOrBlank().not() && taskDate.isNullOrBlank().not()) {
                    val intent = Intent(this@EditTaskActivity, TaskActivity::class.java)
                    if (task != null) {
                        intent.putExtra(
                            "userTask",
                            UserTask(
                                task.taskId,
                                taskContent.toString(),
                                calendar.time,
                                task.isDone
                            )
                        )

                    } else {
                        intent.putExtra(
                            "userTask",
                            UserTask(1, taskContent.toString(), calendar.time, false)
                        )
                    }
                    setResult(0, intent)
                    finish()
                }
            }
            dateEdit.setOnClickListener {
                chooseTaskDate()
            }

        }
    }

    private fun chooseTaskDate() {
        val dialog = DatePickerDialog(
            this,
            android.R.style.Theme_Material_Light_Dialog,
            { _, cYear, cMonth, cDay ->
                Log.d("date", cDay.toString())
                run {
                    calendar.set(
                        cYear,
                        cMonth,
                        cDay,
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE)
                    )
                    binding.dateEdit.text = DATE_FORMATTER.format(calendar.time)
                    year = cYear
                    month = cMonth
                    day = cDay
                }
            },
            year,
            month,
            day
        )
        val color = ContextCompat.getColor(this, R.color.highlight)
        dialog.show()
        dialog.getButton(DatePickerDialog.BUTTON_POSITIVE)
            .setTextColor(color)
        dialog.getButton(DatePickerDialog.BUTTON_NEGATIVE)
            .setTextColor(color)
    }

    companion object {
        private val DATE_FORMATTER =
            SimpleDateFormat(TaskApplication.getString(R.string.task_format), Locale.ENGLISH)
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