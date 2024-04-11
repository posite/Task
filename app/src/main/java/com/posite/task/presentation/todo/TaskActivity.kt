package com.posite.task.presentation.todo

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.posite.task.R
import com.posite.task.databinding.ActivityTaskBinding
import com.posite.task.presentation.base.BaseActivity
import com.posite.task.presentation.regist.model.UserInfo
import com.posite.task.presentation.todo.adapter.TaskListAdapter
import com.posite.task.presentation.todo.model.UserTask
import com.posite.task.presentation.todo.vm.TaskViewModel
import com.posite.task.presentation.todo.vm.TaskViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Date

@AndroidEntryPoint
class TaskActivity : BaseActivity<ActivityTaskBinding>(R.layout.activity_task) {
    private val viewModel: TaskViewModel by viewModels<TaskViewModelImpl>()
    private val taskAdapter by lazy {
        TaskListAdapter {

        }
    }

    private val activityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == 0) {
                val taskIntent = it.data
                try {
                    val userTask = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        taskIntent!!.getParcelableExtra("userTask", UserTask::class.java)
                    } else {
                        taskIntent!!.getParcelableExtra<UserTask>("userTask")
                    }
                    userTask?.let { task ->
                        viewModel.addTask(task)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }


    override fun initView() {
        binding.userProfile.clipToOutline = true
        setProfile()
        binding.taskRv.adapter = taskAdapter
        binding.taskRv.layoutManager = LinearLayoutManager(this)
        


        binding.addBtn.setOnClickListener {
            val intent = Intent(this, EditTaskActivity::class.java)
            if (taskAdapter.itemCount != 0) {
                intent.putExtra(
                    "userTask",
                    UserTask(taskAdapter.getLastItem().taskId + 1, "", Date(), false)
                )
            } else {
                intent.putExtra("userTask", UserTask(1, "", Date(), false))
            }
            intent.putExtra("edit_type", EDIT_TYPE_ADD)
            activityResultLauncher.launch(intent)
        }

    }

    private fun setProfile() {
        val userInfo: UserInfo? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("userInfo", UserInfo::class.java)
        } else {
            intent.getParcelableExtra<UserInfo>("userInfo")
        }

        userInfo?.let {
            binding.userProfile.setImageBitmap(it.profile)
        }
    }

    override fun initObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.taskList.collect {
                    taskAdapter.submitList(it)
                }
            }
        }
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

    companion object {
        private const val EDIT_TYPE_ADD = "add"
        private const val EDIT_TYPE_EDIT = "edit"
    }
}