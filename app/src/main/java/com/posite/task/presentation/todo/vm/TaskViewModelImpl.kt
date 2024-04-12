package com.posite.task.presentation.todo.vm

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.posite.task.R
import com.posite.task.TaskApplication.Companion.getString
import com.posite.task.data.todo.TodoDao
import com.posite.task.presentation.base.BaseViewModel
import com.posite.task.presentation.regist.model.UserInfo
import com.posite.task.presentation.todo.model.UserTask
import com.posite.task.util.BitmapConverter
import com.posite.task.util.DataStoreUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import javax.inject.Inject

@HiltViewModel
class TaskViewModelImpl @Inject constructor(
    private val dao: TodoDao,
    private val dataStore: DataStoreUtil
) : TaskViewModel,
    BaseViewModel() {
    private val _taskList: MutableStateFlow<List<UserTask>> = MutableStateFlow(emptyList())
    override val taskList: StateFlow<List<UserTask>>
        get() = _taskList

    private val _userInfo: MutableStateFlow<UserInfo> =
        MutableStateFlow(UserInfo("", "", null))
    override val userInfo: StateFlow<UserInfo>
        get() = _userInfo

    override fun getAllTask() {
        viewModelScope.launch {
            _taskList.emit(dao.getAllTask())
        }
    }

    override fun addTask(task: UserTask) {
        viewModelScope.launch {
            Log.d("dao", "add")
            dao.addTask(task)
            _taskList.emit(dao.getAllTask())
        }
    }

    override fun updateTask(task: UserTask) {
        viewModelScope.launch {
            Log.d("dao", "update")
            dao.updateTask(task)
            _taskList.emit(dao.getAllTask())
        }
    }

    override fun removeTask(task: UserTask) {
        viewModelScope.launch {
            Log.d("dao", "delete")
            dao.deleteTask(task)
            _taskList.emit(dao.getAllTask())
        }
    }

    override fun getUserInfo() {
        viewModelScope.launch {
            if (dataStore.loadUserBirthday() != null && dataStore.loadUserProfile()
                    .isNotBlank() && dataStore.loadUserName().isNotBlank()
            ) {
                _userInfo.emit(
                    UserInfo(
                        dataStore.loadUserName(),
                        SimpleDateFormat(getString(R.string.date_format)).format(dataStore.loadUserBirthday()),
                        BitmapConverter.stringToBitmap(dataStore.loadUserProfile())!!
                    )
                )
            }
        }
    }
}