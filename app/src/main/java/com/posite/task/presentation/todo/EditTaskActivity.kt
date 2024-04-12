package com.posite.task.presentation.todo

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import com.google.android.material.textfield.TextInputEditText
import com.posite.task.R
import com.posite.task.TaskApplication
import com.posite.task.databinding.ActivityEditTaskBinding
import com.posite.task.presentation.base.BaseActivity
import com.posite.task.presentation.todo.dialog.DateDialog
import com.posite.task.presentation.todo.model.UserTask
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class EditTaskActivity : BaseActivity<ActivityEditTaskBinding>(R.layout.activity_edit_task),
    DateDialog.OnInputListener {
    private var calendar = Calendar.getInstance()
    private var task: UserTask? = null
    private var type: String? = null

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            backpressLoadData()
        }
    }

    override fun initObserver() {

    }

    override fun initView() {
        this.onBackPressedDispatcher.addCallback(this, callback)
        task = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("user_task", UserTask::class.java)
        } else {
            intent.getParcelableExtra<UserTask>("user_task")
        }
        type = intent.getStringExtra("edit_type")

        with(binding) {
            when (type) {
                "add" -> {
                    editTaskTitle.text = getString(R.string.add_task_title)
                    finishEditBtn.text = getString(R.string.add_btn)
                }

                "edit" -> {
                    editTaskTitle.text = getString(R.string.edit_task_title)
                    taskEdit.setText(task!!.taskTitle)
                    finishEditBtn.text = getString(R.string.edit_btn)
                    dateEdit.text = DATE_FORMATTER.format(task!!.date)
                    calendar.time = task!!.date
                }
            }
            finishEditBtn.setOnClickListener {
                loadData()
            }
            dateEdit.setOnClickListener {
                val dialog = DateDialog(calendar)
                dialog.show(supportFragmentManager, "date_dialog")
                //chooseTaskDate()
            }

        }
    }

    private fun loadData() {
        val taskContent = binding.taskEdit.text
        val taskDate = binding.dateEdit.text
        if (taskContent.isNullOrBlank().not() && taskDate.isNullOrBlank().not()) {
            val intent = Intent(this@EditTaskActivity, TaskActivity::class.java)
            task = UserTask(
                taskTitle = taskContent.toString(),
                date = calendar.time,
                isDone = task!!.isDone
            )
            if (type == "edit") {
                intent.putExtra("user_task", task)
                setResult(1, intent)
            } else {
                intent.putExtra("user_task", task)
                setResult(0, intent)
            }
            finish()
        }
    }

    private fun backpressLoadData() {
        val intent = Intent(this@EditTaskActivity, TaskActivity::class.java)
        if (type == "edit") {
            intent.putExtra("user_task", task)
            setResult(2, intent)
        } else {
            setResult(3, intent)
        }
        finish()
    }

    private fun chooseTaskDate() {
        /*
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

         */
    }


    companion object {
        private val DATE_FORMATTER =
            SimpleDateFormat(TaskApplication.getString(R.string.full_date_format), Locale.ENGLISH)
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

    override fun sendResult(date: Calendar) {
        calendar = date
        binding.dateEdit.text = DATE_FORMATTER.format(calendar.time)
    }

}