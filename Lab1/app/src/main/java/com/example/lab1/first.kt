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

class first : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab1Theme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    FirstTaskApp(
                        modifier = Modifier.padding(innerPadding),
                        onBackPressed = { finish() }
                    )
                }
            }
        }
    }
}

@Composable
fun FirstTaskApp(modifier: Modifier = Modifier, onBackPressed: () -> Unit) {
    // input
    var carbon by remember { mutableStateOf("") }
    var hydrogen by remember { mutableStateOf("") }
    var oxygen by remember { mutableStateOf("") }
    var sulfur by remember { mutableStateOf("") }
    var nitrogen by remember { mutableStateOf("") }
    var moisture by remember { mutableStateOf("") }
    var ash by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    // Calculate
    fun calculateFirstTask() {
        val H_P = hydrogen.toDoubleOrNull() ?: 0.0
        val C_P = carbon.toDoubleOrNull() ?: 0.0
        val S_P = sulfur.toDoubleOrNull() ?: 0.0
        val N_P = nitrogen.toDoubleOrNull() ?: 0.0
        val O_P = oxygen.toDoubleOrNull() ?: 0.0
        val W_P = moisture.toDoubleOrNull() ?: 0.0
        val A_P = ash.toDoubleOrNull() ?: 0.0

        // cof
        val K_RS = 100 / (100 - W_P)
        val K_RG = 100 / (100 - W_P - A_P)

        // суха маса
        val H_S = H_P * K_RS
        val C_S = C_P * K_RS
        val S_S = S_P * K_RS
        val N_S = N_P * K_RS
        val O_S = O_P * K_RS

        // горюча маса
        val H_G = H_P * K_RG
        val C_G = C_P * K_RG
        val S_G = S_P * K_RG
        val N_G = N_P * K_RG
        val O_G = O_P * K_RG

        // low temp
        val Q_RH = 339 * C_P + 1030 * H_P - 108.8 * (O_P - S_P) - 25 * W_P

        // output
        result = """
            Склад сухої маси:
            H: %.2f%%, C: %.2f%%, S: %.2f%%, N: %.2f%%, O: %.2f%%
            Склад паливної маси:
            H: %.2f%%, C: %.2f%%, S: %.2f%%, N: %.2f%%, O: %.2f%%
            Нижня теплота згоряння (робоча маса): %.4f МДж/кг
        """.trimIndent().format(H_S, C_S, S_S, N_S, O_S, H_G, C_G, S_G, N_G, O_G, Q_RH)
    }

    Column(modifier = modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        TextField(
            value = carbon,
            onValueChange = { carbon = it },
            label = { Text("Вуглець (C_P, %)") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = hydrogen,
            onValueChange = { hydrogen = it },
            label = { Text("Водень (H_P, %)") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = oxygen,
            onValueChange = { oxygen = it },
            label = { Text("Кисень (O_P, %)") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = sulfur,
            onValueChange = { sulfur = it },
            label = { Text("Сірка (S_P, %)") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = nitrogen,
            onValueChange = { nitrogen = it },
            label = { Text("Азот (N_P, %)") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = moisture,
            onValueChange = { moisture = it },
            label = { Text("Вологість (W_P, %)") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = ash,
            onValueChange = { ash = it },
            label = { Text("Зольність (A_P, %)") },
            modifier = Modifier.fillMaxWidth()
        )


        Button(onClick = { calculateFirstTask() }, modifier = Modifier.fillMaxWidth()) {
            Text("Розрахувати")
        }

        Button(onClick = onBackPressed, modifier = Modifier.fillMaxWidth()) {
            Text("Повернуться")
        }

        Text(text = result, modifier = Modifier.padding(top = 16.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun FirstTaskPreview() {
    Lab1Theme() {
        FirstTaskApp(onBackPressed = {})
    }
}
