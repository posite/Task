package com.posite.task.data.todo

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.posite.task.presentation.todo.model.UserTask

@Dao
interface TodoDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addTask(item: UserTask)

    @Query("SELECT * FROM UserTask")
    suspend fun getAllTask(): List<UserTask>

    @Update
    suspend fun updateTask(task: UserTask)

    @Delete
    suspend fun deleteTask(task: UserTask)
}