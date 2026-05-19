package com.guanpj.me.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.guanpj.me.todolist.ui.theme.TodoListTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoListTheme {
                TodoListApp()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TodoListApp(viewModel: TaskViewModel = viewModel()) {
    var showDialog by remember { mutableStateOf(false) }
    val taskList by viewModel.list.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TitleBar()
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showDialog = true
                }
            ) {
                Text("+", style = MaterialTheme.typography.headlineMedium)
            }
        },
        containerColor = MaterialTheme.colorScheme.background) { innerPadding ->
        HomePage(
            modifier = Modifier.padding(innerPadding),
            taskList = taskList,
            onToggleTask = viewModel::toggleTask,
            onDeleteTask = viewModel::deleteTask
        )
    }

    if (showDialog) {
        AddTaskDialog(onDismiss = {
            showDialog = false
        }) {
            viewModel.addTask(it)
            showDialog = false
        }
    }
}

@Composable
fun AddTaskDialog(onDismiss: () -> Unit, onAdd: (String) -> Unit) {
    var title by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Add Task")
        },
        text = {
            OutlinedTextField(
                value = title,
                onValueChange = {
                    title = it
                },
                label = {
                    Text("Add Title")
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(
                onClick = { onAdd(title) },
                enabled = title.isNotBlank()
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleBar() {
    TopAppBar(title = {
        Column {
            Text("Todo List", fontWeight = FontWeight.Bold)
            Text("1 个待办 2 个已完成")
        }
    }, colors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.surface
    ))
}

@Composable
fun HomePage(
    modifier: Modifier,
    taskList: List<Task>,
    onToggleTask: (Task) -> Unit,
    onDeleteTask: (Task) -> Unit
) {
    Column(modifier = modifier) {
        SummaryStrip(5, 4, 3)

        LazyColumn {
            items(taskList, key = { it.id }) { task ->
                TaskItem(
                    task = task,
                    onCheckedChange = {
                        onToggleTask(task)
                    },
                    onDelete = {
                        onDeleteTask(task)
                    }
                )
            }
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    onCheckedChange: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isDone,
                onCheckedChange = { onCheckedChange() }
            )

            Text(
                text = task.title,
                style = MaterialTheme.typography.bodyLarge,
                textDecoration = if (task.isDone) TextDecoration.LineThrough else TextDecoration.None,
                color = if (task.isDone) {
                    MaterialTheme.colorScheme.onSurfaceVariant
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
            Spacer(Modifier.width(8.dp))
            TextButton(onClick = onDelete) {
                Text("删除")
            }
        }
    }
}

@Composable
fun SummaryStrip(
    totalCount: Int,
    activeCount: Int,
    doneCount: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        SummaryCard(Modifier.weight(1f), totalCount, "Total", Color(0xFF166B6B))
        SummaryCard(Modifier.weight(1f), activeCount, "Todo", Color(0xFF7A5C12))
        SummaryCard(Modifier.weight(1f), doneCount, "Done", Color(0xFF4C6275))
    }
}

@Composable
fun SummaryCard(modifier: Modifier, count: Int, status: String, accent: Color) {
    Card(
        modifier = modifier.height(86.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            ) {
            Text(
                text = count.toString(),
                color = accent,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = status,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}