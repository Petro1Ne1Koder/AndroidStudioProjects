package com.example.reliabilitycalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class Task2Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Task2Screen(onBackPressed = { finish() })
            }
        }
    }
}

@Composable
fun Task2Screen(onBackPressed: () -> Unit) {
    var omega by remember { mutableStateOf("") }
    var tb by remember { mutableStateOf("") }
    var Pm by remember { mutableStateOf("") }
    var Tm by remember { mutableStateOf("") }
    var kp by remember { mutableStateOf("") }
    var zPerA by remember { mutableStateOf("") }
    var zPerP by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    fun calculateTask2() {
        val omegaValue = omega.toDoubleOrNull() ?: 0.0 // ω
        val tbValue = tb.toDoubleOrNull() ?: 0.0       // tb (t_b, середній час відновлення у роках)
        val PmValue = Pm.toDoubleOrNull() ?: 0.0       // Pm
        val TmValue = Tm.toDoubleOrNull() ?: 0.0       // Tm
        val kpValue = kp.toDoubleOrNull() ?: 0.0       // kp
        val zPerAValue = zPerA.toDoubleOrNull() ?: 0.0 // Zпер.а
        val zPerPValue = zPerP.toDoubleOrNull() ?: 0.0 // Zпер.п

        val MWA = omegaValue * tbValue * PmValue * TmValue
        val MWP = kpValue * PmValue * TmValue
        val M = zPerAValue * MWA + zPerPValue * MWP

        fun round(value: Double): String {
            return "%.5f".format(value)
        }

        result = """
            Математичне сподівання аварійного недовідпущення: ${round(MWA)} кВт·год
            Математичне сподівання планового недовідпущення: ${round(MWP)} кВт·год
            Математичне сподівання збитків від перервання електропостачання: ${round(M)} грн
        """.trimIndent()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextField(
            value = omega,
            onValueChange = { omega = it },
            label = { Text("Частота відмов ω") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = tb,
            onValueChange = { tb = it },
            label = { Text("Середній час відновлення t_b (у роках)") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = Pm,
            onValueChange = { Pm = it },
            label = { Text("Середнє споживання потужності Pm (кВт·год)") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = Tm,
            onValueChange = { Tm = it },
            label = { Text("Тривалість Tm (години)") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = kp,
            onValueChange = { kp = it },
            label = { Text("Коефіцієнт kp") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = zPerA,
            onValueChange = { zPerA = it },
            label = { Text("Zпер.а (грн/кВт·год)") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = zPerP,
            onValueChange = { zPerP = it },
            label = { Text("Zпер.п (грн/кВт·год)") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(onClick = { calculateTask2() }, modifier = Modifier.fillMaxWidth()) {
            Text("Розрахувати")
        }
        Button(onClick = onBackPressed, modifier = Modifier.fillMaxWidth()) {
            Text("Назад")
        }
        Text(text = result, modifier = Modifier.padding(top = 16.dp))
    }
}
