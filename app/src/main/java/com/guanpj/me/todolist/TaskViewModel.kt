package com.guanpj.me.todolist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TaskViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = TaskRepository(app)

    private val _list = MutableStateFlow(repository.loadTask())
    val list = _list.asStateFlow()

    fun addTask(title: String) {
        val cleanedTitle = title.trim()
        if (cleanedTitle.isEmpty()) return

        _list.value = listOf(
            Task(id = System.currentTimeMillis(), title = title)
        ) + _list.value
        save()
    }

    fun toggleTask(taskId: Long) {
        _list.value = _list.value.map {
            if (it.id == taskId) it.copy(isDone = !it.isDone) else it
        }
        save()
    }

    fun deleteTask(taskId: Long) {
        _list.value = _list.value.filter { it.id != taskId }
        save()
    }

    fun save() {
        repository.saveTask(_list.value)
    }
}