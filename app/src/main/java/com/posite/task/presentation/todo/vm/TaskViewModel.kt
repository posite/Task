package com.posite.task.presentation.todo.vm

import com.posite.task.presentation.todo.model.UserTask
import kotlinx.coroutines.flow.StateFlow

interface TaskViewModel {
    val taskList: StateFlow<List<UserTask>>

    fun addTask(task: UserTask)
    fun removeTask(task: UserTask)
}