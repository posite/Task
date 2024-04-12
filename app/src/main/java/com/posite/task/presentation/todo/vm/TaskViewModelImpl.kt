package com.posite.task.presentation.todo.vm

import androidx.lifecycle.viewModelScope
import com.posite.task.data.todo.TodoDB
import com.posite.task.presentation.base.BaseViewModel
import com.posite.task.presentation.todo.model.UserTask
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModelImpl @Inject constructor(private val db: TodoDB) : TaskViewModel,
    BaseViewModel() {
    private val _taskList: MutableStateFlow<List<UserTask>> = MutableStateFlow(emptyList())
    override val taskList: StateFlow<List<UserTask>>
        get() = _taskList

    override fun addTask(task: UserTask) {
        viewModelScope.launch {
            _taskList.emit(_taskList.value + task)
        }
    }

    override fun removeTask(task: UserTask) {
        viewModelScope.launch {
            _taskList.emit(_taskList.value - _taskList.value.find { it.taskId == task.taskId }!!)
        }
    }

}