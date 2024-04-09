package com.posite.task.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserInfo(
    val name: String,
    val birthday: String
) : Parcelable
