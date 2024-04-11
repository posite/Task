package com.posite.task.presentation.todo.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class UserTask(
    val taskId: Long,
    val taskTitle: String,
    val date: Date,
    val isDone: Boolean
) : Parcelable
