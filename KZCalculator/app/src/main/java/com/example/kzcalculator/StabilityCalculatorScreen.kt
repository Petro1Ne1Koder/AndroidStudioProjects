package com.example.kzcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class StabilityCalculatorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                StabilityCalculatorScreen(onBackPressed = { finish() })
            }
        }
    }
}

@Composable
fun StabilityCalculatorScreen(onBackPressed: () -> Unit) {
    var current by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    fun calculateStability() {
        val i = current.toDoubleOrNull()
        val t = duration.toDoubleOrNull()

        result = if (i != null && t != null) {
            "Термічна стійкість: %.2f A²·с".format(i * i * t)
        } else {
            "Помилка: перевірте введення даних."
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextField(
            value = current,
            onValueChange = { current = it },
            label = { Text("Ток (А)") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = duration,
            onValueChange = { duration = it },
            label = { Text("Тривалість (с)") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(onClick = { calculateStability() }, modifier = Modifier.fillMaxWidth()) {
            Text("Розрахувати")
        }
        Button(onClick = onBackPressed, modifier = Modifier.fillMaxWidth()) {
            Text("Повернутись")
        }
        Text(text = result, modifier = Modifier.padding(top = 16.dp))
    }
}
