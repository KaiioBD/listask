package com.example.listask.model

import java.util.Date

data class Tarea (
    val id: String  = "",
    val name: String  = "",
    val description: String  = "",
    val date: Date = Date(),
    val userId: String  = ""
)