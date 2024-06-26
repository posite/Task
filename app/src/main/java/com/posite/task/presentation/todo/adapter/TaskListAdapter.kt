package com.posite.task.presentation.todo.adapter

import android.icu.text.SimpleDateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.posite.task.R
import com.posite.task.TaskApplication
import com.posite.task.databinding.ItemTaskBinding
import com.posite.task.presentation.todo.model.UserTask
import java.util.Locale

class TaskListAdapter(
    val editTask: (UserTask) -> Unit,
    val removeTask: (UserTask) -> Unit,
    val addTask: (UserTask) -> Unit
) :
    ListAdapter<UserTask, TaskListAdapter.ItemViewHolder>(differ) {

    inner class ItemViewHolder(
        private val binding: ItemTaskBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: UserTask) {
            Log.d("task_id", data.taskId.toString())
            binding.userTaskTitle.text = data.taskTitle
            binding.userTaskDate.text = DATE_FORMATTER.format(data.date)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        return ItemViewHolder(
            ItemTaskBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    fun onItemMoved(index: Int): UserTask = getItem(index)


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val differ = object : DiffUtil.ItemCallback<UserTask>() {
            override fun areItemsTheSame(oldItem: UserTask, newItem: UserTask): Boolean {
                return oldItem.taskId == newItem.taskId
            }

            override fun areContentsTheSame(oldItem: UserTask, newItem: UserTask): Boolean {
                return oldItem == newItem
            }
        }

        private val DATE_FORMATTER =
            SimpleDateFormat(TaskApplication.getString(R.string.full_date_format), Locale.ENGLISH)
    }
}