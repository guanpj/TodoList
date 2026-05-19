package com.guanpj.me.todolist

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    @PrimaryKey val id: Long,
    val title: String,
    val time: Long = System.currentTimeMillis(),
    val isDone: Boolean = false
)
