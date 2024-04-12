package com.posite.task.data.todo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
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

    companion object {
        private var instance: TodoDB? = null

        @Synchronized
        fun getInstance(context: Context): TodoDB? {
            if (instance == null) {
                synchronized(TodoDB::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TodoDB::class.java,
                        "todo_database"
                    ).build()
                }
            }
            return instance
        }
    }
}