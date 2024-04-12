package com.posite.task.data.todo

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.posite.task.presentation.todo.model.Converter
import com.posite.task.presentation.todo.model.UserTask

@Database(
    entities = [UserTask::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converter::class)
abstract class TodoDB : RoomDatabase() {
    abstract fun todoDao(): TodoDao

}