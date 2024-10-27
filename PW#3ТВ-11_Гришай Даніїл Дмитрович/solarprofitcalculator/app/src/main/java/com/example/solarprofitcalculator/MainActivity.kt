package com.example.solarprofitcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.sqrt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    SolarProfitApp(
                        modifier = Modifier.padding(innerPadding),
                        closeApp = { finish() }
                    )
                }
            }
        }
    }
}

@Composable
fun SolarProfitApp(modifier: Modifier = Modifier, closeApp: () -> Unit) {
    var inputPower by remember { mutableStateOf("") }
    var firstErrorMargin by remember { mutableStateOf("") }
    var secondErrorMargin by remember { mutableStateOf("") }
    var electricityRate by remember { mutableStateOf("") }
    var resultText by remember { mutableStateOf("") }

    fun calculateGaussian(x: Double, mean: Double, deviation: Double): Double {
        val coefficient = 1 / (deviation * sqrt(2 * Math.PI))
        val exponent = -((x - mean).pow(2)) / (2 * deviation.pow(2))
        return coefficient * exp(exponent)
    }

    fun approximateIntegral(start: Double, end: Double, intervals: Int, mean: Double, deviation: Double): Double {
        var sum = 0.0
        val stepSize = (end - start) / intervals
        for (i in 0 until intervals) {
            val left = start + i * stepSize
            val right = start + (i + 1) * stepSize
            sum += (calculateGaussian(left, mean, deviation) + calculateGaussian(right, mean, deviation)) / 2 * stepSize
        }
        return sum
    }

    fun performCalculation() {
        val power = inputPower.toDoubleOrNull() ?: 0.0
        val initialError = firstErrorMargin.toDoubleOrNull() ?: 0.0
        val improvedError = secondErrorMargin.toDoubleOrNull() ?: 0.0
        val ratePerKWh = electricityRate.toDoubleOrNull() ?: 0.0

        val rangeStart = power - improvedError
        val rangeEnd = power + improvedError
        val divisions = 1000

        // Доля мощности без отклонений до и после улучшения
        val efficiencyBefore = approximateIntegral(rangeStart, rangeEnd, divisions, power, initialError)
        val earningsBefore = power * 24 * efficiencyBefore * ratePerKWh * 1000
        val penaltiesBefore = power * 24 * (1 - efficiencyBefore) * ratePerKWh * 1000

        val efficiencyAfter = approximateIntegral(rangeStart, rangeEnd, divisions, power, improvedError)
        val earningsAfter = power * 24 * efficiencyAfter * ratePerKWh * 1000
        val penaltiesAfter = power * 24 * (1 - efficiencyAfter) * ratePerKWh * 1000

        resultText = """
            Прибуток до вдосконалення: %.2f тис. грн
            Виручка до вдосконалення: %.2f тис. грн
            Штраф до вдосконалення: %.2f тис. грн
            Прибуток після вдосконалення: %.2f тис. грн
            Виручка після вдосконалення: %.2f тис. грн
            Штраф після вдосконалення: %.2f тис. грн
        """.trimIndent().format(
            earningsBefore / 1000,
            (earningsBefore - penaltiesBefore) / 1000,
            penaltiesBefore / 1000,
            earningsAfter / 1000,
            (earningsAfter - penaltiesAfter) / 1000,
            penaltiesAfter / 1000
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextField(
            value = inputPower,
            onValueChange = { inputPower = it },
            label = { Text("Потужність (МВт)") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = firstErrorMargin,
            onValueChange = { firstErrorMargin = it },
            label = { Text("Перше відхилення (МВт)") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = secondErrorMargin,
            onValueChange = { secondErrorMargin = it },
            label = { Text("Друге відхилення (МВт)") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = electricityRate,
            onValueChange = { electricityRate = it },
            label = { Text("Вартість електроенергії (грн/кВт·год)") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(onClick = { performCalculation() }, modifier = Modifier.fillMaxWidth()) {
            Text("Розрахувати прибуток")
        }

        Text(text = resultText, modifier = Modifier.padding(top = 16.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun SolarProfitCalculatorPreview() {
    MaterialTheme {
        SolarProfitApp(closeApp = {})
    }
}
