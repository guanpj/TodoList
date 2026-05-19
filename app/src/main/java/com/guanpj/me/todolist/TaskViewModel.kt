package com.guanpj.me.todolist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(app: Application) : AndroidViewModel(app) {

    private val dao = AppDatabase.getInstance(app).taskDao()
    private val repository = TaskRepository(dao)

    val list: StateFlow<List<Task>> = repository.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addTask(title: String) {
        val cleanedTitle = title.trim()
        if (cleanedTitle.isEmpty()) return

        viewModelScope.launch {
            repository.insert(Task(id = System.currentTimeMillis(), title = title))
        }
    }

    fun toggleTask(task: Task) {
        viewModelScope.launch {
            repository.update(task.copy(isDone = !task.isDone))
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.delete(task)
        }
    }
}
