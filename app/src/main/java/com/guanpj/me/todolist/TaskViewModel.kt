package com.guanpj.me.todolist

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class TaskViewModel(app: Application) : AndroidViewModel(app) {

    val repository = TaskRepository(app)
    val list = mutableListOf<Task>()

    init {
        list.addAll(repository.loadTask())
    }

    fun addTask(title: String) {
        val cleanedTitle = title.trim()
        if (cleanedTitle.isEmpty()) return
        list.add(0, Task(
            id = System.currentTimeMillis(),
            title = title,
        ))
    }


    fun save() {
        repository.saveTask(list)
    }
}