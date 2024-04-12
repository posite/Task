package com.posite.task.presentation.todo.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
@Entity
data class UserTask(
    @PrimaryKey(autoGenerate = true)
    val taskId: Long = 0L,
    val taskTitle: String,
    val date: Date,
    val isDone: Boolean
) : Parcelable

class Converter {
    @TypeConverter
    fun dateToString(date: Date): String? {
        return date.let { date.time.toString() }
    }

    @TypeConverter
    fun StringToDate(value: String): Date? {
        return value.let { Date(it.toLong()) }
    }

}
