package com.posite.task.di

import android.content.Context
import androidx.room.Room
import com.posite.task.data.todo.TodoDB
import com.posite.task.data.todo.TodoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DBModule {

    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): TodoDB = Room
        .databaseBuilder(context, TodoDB::class.java, "kim_ready.db")
        .build()

    @Singleton
    @Provides
    fun provideTodoDao(appDatabase: TodoDB): TodoDao = appDatabase.todoDao()
}