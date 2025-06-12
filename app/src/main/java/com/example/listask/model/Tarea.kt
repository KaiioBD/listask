package com.example.listask.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date


@Parcelize
data class Tarea(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val date: Date = Date(),
    val userId: String = ""
) : Parcelable