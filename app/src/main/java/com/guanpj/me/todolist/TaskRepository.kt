package com.guanpj.me.todolist

import android.content.Context
import androidx.compose.ui.util.fastForEach
import androidx.core.content.edit
import org.json.JSONArray
import org.json.JSONObject

class TaskRepository(context: Context) {

    companion object {
        const val KEY = "task"
    }
    private val prefs = context.getSharedPreferences("todo_list", Context.MODE_PRIVATE)

    fun loadTask(): List<Task> {
        val raw = prefs.getString(KEY, "")
        val array = JSONArray(raw)
        return List(array.length()) { index ->
            val cur = array.getJSONObject(index)
            Task(
                id = cur.getLong("id"),
                title = cur.getString("title"),
                time = cur.getLong("time"),
                isDone = cur.getBoolean("isDone")
            )
        }
    }

    fun saveTask(taskList: List<Task>) {
        val arr = JSONArray()
        taskList.fastForEach { task ->
            arr.put(JSONObject()
                .put("id", task.id)
                .put("title", task.title)
                .put("time", task.time)
                .put("isDone", task.isDone))
        }

        prefs.edit {
            putString(KEY, arr.toString())
        }
    }
}