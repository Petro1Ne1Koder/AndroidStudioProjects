package com.example.reliabilitycalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class Task1Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Task1Screen(onBackPressed = { finish() })
            }
        }
    }
}

@Composable
fun Task1Screen(onBackPressed: () -> Unit) {
    var userInput by remember { mutableStateOf("") }
    var nValue by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    val epsElements = mapOf(
        1 to "ПЛ-110 кВ",
        2 to "ПЛ-35 кВ",
        3 to "ПЛ-10 кВ",
        4 to "КЛ-10 кВ (траншея)",
        5 to "КЛ-10 кВ (кабельний канал)",
        6 to "Т-110 кВ",
        7 to "Т-35 кВ",
        8 to "Т-10 кВ (кабельна мережа 10 кВ)",
        9 to "Т-10 кВ (повітряна мережа 10 кВ)",
        10 to "В-110 кВ (елегазовий)",
        11 to "В-10 кВ (малооливний)",
        12 to "В-10 кВ (вакуумний)",
        13 to "АВ-0.38 кВ",
        14 to "ЕД 6, 10 кВ",
        15 to "ЕД 0,38 кВ"
    )

    val elementData = mapOf(
        "ПЛ-110 кВ" to mapOf("omega" to 0.07, "tv" to 10.0, "tp" to 35.0),
        "ПЛ-35 кВ" to mapOf("omega" to 0.02, "tv" to 8.0, "tp" to 35.0),
        "ПЛ-10 кВ" to mapOf("omega" to 0.02, "tv" to 10.0, "tp" to 35.0),
        "КЛ-10 кВ (траншея)" to mapOf("omega" to 0.03, "tv" to 44.0, "tp" to 9.0),
        "КЛ-10 кВ (кабельний канал)" to mapOf("omega" to 0.005, "tv" to 17.5, "tp" to 9.0),
        "Т-110 кВ" to mapOf("omega" to 0.015, "tv" to 100.0, "tp" to 43.0),
        "Т-35 кВ" to mapOf("omega" to 0.02, "tv" to 80.0, "tp" to 28.0),
        "Т-10 кВ (кабельна мережа 10 кВ)" to mapOf("omega" to 0.005, "tv" to 60.0, "tp" to 10.0),
        "Т-10 кВ (повітряна мережа 10 кВ)" to mapOf("omega" to 0.05, "tv" to 60.0, "tp" to 10.0)
    )

    fun calculateTask1() {
        val selectedIndexes = userInput.split(" ").mapNotNull { it.toIntOrNull() }
        val selectedElements = selectedIndexes.mapNotNull { epsElements[it] }
        val n = nValue.toDoubleOrNull() ?: return

        var omegaSum = 0.0
        var tRecovery = 0.0
        var maxTp = 0.0

        for (element in selectedElements) {
            val data = elementData[element] ?: continue
            omegaSum += data["omega"] ?: 0.0
            tRecovery += (data["omega"] ?: 0.0) * (data["tv"] ?: 0.0)
            maxTp = maxOf(maxTp, data["tp"] ?: 0.0)
        }

        omegaSum += 0.03 * n
        tRecovery += 0.06 * n
        tRecovery /= omegaSum

        val kAP = omegaSum * tRecovery / 8760
        val kPP = 1.2 * maxTp / 8760

        val omegaDK = 2 * omegaSum * (kAP + kPP)
        val omegaDKS = omegaDK + 0.02

        result = """
        Частота відмов одноколової системи: ${"%.5f".format(omegaSum)} рік^-1
        Середня тривалість відновлення: ${"%.5f".format(tRecovery)} год
        Коефіцієнт аварійного простою: ${"%.5f".format(kAP)}
        Коефіцієнт планового простою: ${"%.5f".format(kPP)}
        Частота відмов одночасно двох кіл двоколової системи: ${"%.5f".format(omegaDK)} рік^-1
        Частота відмов двоколової системи з урахуванням секційного вимикача: ${"%.5f".format(omegaDKS)} рік^-1
    """.trimIndent()
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextField(
            value = userInput,
            onValueChange = { userInput = it },
            label = { Text("Введіть ключі елементів (через пробел): 1, 2, ...") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = nValue,
            onValueChange = { nValue = it },
            label = { Text("Введіть значення N") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(onClick = { calculateTask1() }, modifier = Modifier.fillMaxWidth()) {
            Text("Розрахувати")
        }
        Button(onClick = onBackPressed, modifier = Modifier.fillMaxWidth()) {
            Text("Назад")
        }
        Text(text = result, modifier = Modifier.padding(top = 16.dp))
    }
}
