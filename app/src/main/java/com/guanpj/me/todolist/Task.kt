package com.guanpj.me.todolist

data class Task(
    val id: Long,
    val title: String,
    val time: Long,
    val isDone: Boolean
)
