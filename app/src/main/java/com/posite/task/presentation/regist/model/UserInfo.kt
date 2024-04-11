package com.posite.task.presentation.regist.model

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserInfo(
    val name: String,
    val birthday: String,
    val profile: Bitmap
) : Parcelable
