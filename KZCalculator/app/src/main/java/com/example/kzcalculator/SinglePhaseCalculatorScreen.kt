package com.example.kzcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class SinglePhaseCalculatorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                SinglePhaseCalculatorScreen(onBackPressed = { finish() })
            }
        }
    }
}

@Composable
fun SinglePhaseCalculatorScreen(onBackPressed: () -> Unit) {
    var voltage by remember { mutableStateOf("") }
    var impedance by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    fun calculateSinglePhaseKZ() {
        val v = voltage.toDoubleOrNull()
        val z = impedance.toDoubleOrNull()

        result = if (v != null && z != null && z != 0.0) {
            "Струм однофазного КЗ: %.2f A".format(v / z)
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
            value = voltage,
            onValueChange = { voltage = it },
            label = { Text("Напруга (В)") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = impedance,
            onValueChange = { impedance = it },
            label = { Text("Імпеданс (Ом)") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(onClick = { calculateSinglePhaseKZ() }, modifier = Modifier.fillMaxWidth()) {
            Text("Розрахувати")
        }
        Button(onClick = onBackPressed, modifier = Modifier.fillMaxWidth()) {
            Text("Повернутись")
        }
        Text(text = result, modifier = Modifier.padding(top = 16.dp))
    }
}
