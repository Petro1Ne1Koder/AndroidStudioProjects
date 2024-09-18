package com.example.lab1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lab1.ui.theme.Lab1Theme

class second : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab1Theme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    SecondTaskApp(
                        modifier = Modifier.padding(innerPadding),
                        onBackPressed = { finish() }
                    )
                }
            }
        }
    }
}

@Composable
fun SecondTaskApp(modifier: Modifier = Modifier, onBackPressed: () -> Unit) {
    // input
    var carbon by remember { mutableStateOf("") }
    var hydrogen by remember { mutableStateOf("") }
    var oxygen by remember { mutableStateOf("") }
    var sulfur by remember { mutableStateOf("") }
    var qdaf by remember { mutableStateOf("") }
    var moisture by remember { mutableStateOf("") }
    var ash by remember { mutableStateOf("") }
    var vanadium by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    // Calculate
    fun calculateSecondTask() {
        val C_G = carbon.toDoubleOrNull() ?: 0.0
        val H_G = hydrogen.toDoubleOrNull() ?: 0.0
        val O_G = oxygen.toDoubleOrNull() ?: 0.0
        val S_G = sulfur.toDoubleOrNull() ?: 0.0
        val Q_daf = qdaf.toDoubleOrNull() ?: 0.0
        val W_P = moisture.toDoubleOrNull() ?: 0.0
        val A_P = ash.toDoubleOrNull() ?: 0.0
        val V_G = vanadium.toDoubleOrNull() ?: 0.0

        // Робоча маса
        val factor = (100 - W_P - A_P) / 100
        val C_P = C_G * factor
        val H_P = H_G * factor
        val O_P = O_G * factor
        val S_P = S_G * factor
        val Q_P = Q_daf * factor

        // Ванадій
        val V_P = V_G * (100 - W_P) / 100

        // output
        result = """
            Склад робочої маси:
            Вуглець: %.2f%%
            Водень: %.2f%%
            Кисень: %.2f%%
            Сірка: %.2f%%
            Ванадій: %.2f мг/кг
            Нижня теплота згоряння: %.2f МДж/кг
        """.trimIndent().format(C_P, H_P, O_P, S_P, V_P, Q_P)
    }

    Column(modifier = modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        TextField(
            value = carbon,
            onValueChange = { carbon = it },
            label = { Text("Вуглець (C_G, %)") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = hydrogen,
            onValueChange = { hydrogen = it },
            label = { Text("Водень (H_G, %)") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = oxygen,
            onValueChange = { oxygen = it },
            label = { Text("Кисень (O_G, %)") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = sulfur,
            onValueChange = { sulfur = it },
            label = { Text("Сірка (S_G, %)") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = qdaf,
            onValueChange = { qdaf = it },
            label = { Text("Нижня теплота згоряння (Q_daf, МДж/кг)") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = moisture,
            onValueChange = { moisture = it },
            label = { Text("Вологість робочої маси (W_P, %)") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = ash,
            onValueChange = { ash = it },
            label = { Text("Зольність сухої маси (A_P, %)") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = vanadium,
            onValueChange = { vanadium = it },
            label = { Text("Ванадій (V_G, мг/кг)") },
            modifier = Modifier.fillMaxWidth()
        )


        Button(onClick = { calculateSecondTask() }, modifier = Modifier.fillMaxWidth()) {
            Text("Розрахувати")
        }

        Button(onClick = onBackPressed, modifier = Modifier.fillMaxWidth()) {
            Text("Повернутись")
        }

        Text(text = result, modifier = Modifier.padding(top = 16.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun SecondTaskPreview() {
    Lab1Theme() {
        SecondTaskApp(onBackPressed = {})
    }
}
