package com.guanpj.me.todolist

import kotlinx.coroutines.flow.Flow

class TaskRepository(private val dao: TaskDao) {

    fun getAll(): Flow<List<Task>> = dao.getAll()

    suspend fun insert(task: Task) = dao.insert(task)

    suspend fun update(task: Task) = dao.update(task)

    suspend fun delete(task: Task) = dao.delete(task)
}
