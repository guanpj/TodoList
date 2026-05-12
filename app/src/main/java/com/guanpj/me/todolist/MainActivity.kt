package com.guanpj.me.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.guanpj.me.todolist.ui.theme.TodoListTheme
import org.w3c.dom.Text

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
fun TodoListApp() {
    Scaffold(
        topBar = {
            TitleBar()
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {

                }
            ) {
                Text("+", style = MaterialTheme.typography.headlineMedium)
            }
        },
        containerColor = MaterialTheme.colorScheme.background) { innerPadding ->
        HomePage(modifier = Modifier.padding(innerPadding))
    }
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
fun HomePage(modifier: Modifier) {
    Column(modifier = modifier) {
        SummaryStrip(5, 4, 3)
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