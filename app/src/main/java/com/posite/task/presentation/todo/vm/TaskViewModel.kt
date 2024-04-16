package com.posite.task.presentation.todo.vm

import com.posite.task.presentation.todo.model.UserTask
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface TaskViewModel {
    val taskList: StateFlow<List<UserTask>>
    val profile: SharedFlow<String>

    fun getAllTask()
    fun addTask(task: UserTask)
    fun updateTask(task: UserTask)
    fun removeTask(task: UserTask)

    fun getUserInfo()

}