package com.example.lab1

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lab1.ui.theme.Lab1Theme

class First : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab1Theme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    EmissionCalculatorApp(
                        modifier = Modifier.padding(innerPadding),
                        onBackPressed = { finish() }
                    )
                }
            }
        }
    }
}

@Composable
fun EmissionCalculatorApp(modifier: Modifier = Modifier, onBackPressed: () -> Unit) {
    // Входные значения
    var coal by remember { mutableStateOf("") }
    var fuelOil by remember { mutableStateOf("") }
    var naturalGas by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    // Функция для расчета выбросов
    fun calculateEmissions() {
        // Преобразование введенных значений в числа (по умолчанию 0, если пусто)
        val coalAmount = coal.toDoubleOrNull() ?: 0.0  // Тонны угля
        val fuelOilAmount = fuelOil.toDoubleOrNull() ?: 0.0  // Тонны мазута
        val gasAmount = naturalGas.toDoubleOrNull() ?: 0.0  // Кубометры природного газа

        // Константы для показателей эмиссии и теплоты сгорания (г/ГДж и МДж/кг или м³)
        val emissionFactorCoal = 150.0  // г/ГДж для угля
        val emissionFactorFuelOil = 0.57  // г/ГДж для мазута
        val emissionFactorGas = 0.0  // г/ГДж для природного газа (в данном случае 0)

        val heatValueCoal = 20.47  // МДж/кг для угля
        val heatValueFuelOil = 40.40  // МДж/кг для мазута
        val heatValueGas = 33.08  // МДж/м³ для природного газа

        // Эффективность электростатического фильтра (ЭГА)
        val filterEfficiency = 0.985

        // Логируем входные данные
        Log.d("EmissionCalculator", "Coal Amount (тонн): $coalAmount")
        Log.d("EmissionCalculator", "Fuel Oil Amount (тонн): $fuelOilAmount")
        Log.d("EmissionCalculator", "Natural Gas Amount (м³): $gasAmount")

        // Расчет выбросов для угля
        val totalCoalEmissions = (emissionFactorCoal * heatValueCoal * coalAmount) / 1_000_000  // т до применения фильтра
        val coalEmissionsWithFilter = totalCoalEmissions * (1 - filterEfficiency)  // т после применения фильтра
        Log.d("EmissionCalculator", "Total Coal Emissions after applying filter efficiency: $coalEmissionsWithFilter")

        // Расчет выбросов для мазута
        val totalFuelOilEmissions = (emissionFactorFuelOil * heatValueFuelOil * fuelOilAmount) / 1_000_000  // т до применения фильтра
        val fuelOilEmissionsWithFilter = totalFuelOilEmissions * (1 - filterEfficiency)  // т после применения фильтра
        Log.d("EmissionCalculator", "Total Fuel Oil Emissions after applying filter efficiency: $fuelOilEmissionsWithFilter")


        // Расчет выбросов для природного газа (предполагаем отсутствие твердых частиц)
        val totalGasEmissions = (emissionFactorGas * gasAmount * heatValueGas) / 1_000_000 * (1 - filterEfficiency)  // т
        Log.d("EmissionCalculator", "Total Gas Emissions after applying filter efficiency: $totalGasEmissions")

        // Сумма выбросов
        val totalEmissions = totalCoalEmissions + totalFuelOilEmissions + totalGasEmissions
        Log.d("EmissionCalculator", "Total Emissions (тонн): $totalEmissions")

        // Результат
        result = """
            Валові викиди при спалюванні палива:
            Вугілля: %.4f т
            Мазут: %.4f т
            Природний газ: %.4f т
            Загальна кількість викидів: %.4f т
        """.trimIndent().format(totalCoalEmissions, totalFuelOilEmissions, totalGasEmissions, totalEmissions)
    }

    // Основной интерфейс
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextField(
            value = coal,
            onValueChange = { coal = it },
            label = { Text("Вугілля (в тоннах)") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = fuelOil,
            onValueChange = { fuelOil = it },
            label = { Text("Мазут (в тоннах)") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = naturalGas,
            onValueChange = { naturalGas = it },
            label = { Text("Природний газ (в м³)") },
            modifier = Modifier.fillMaxWidth()
        )

        // Кнопка для расчета
        Button(onClick = { calculateEmissions() }, modifier = Modifier.fillMaxWidth()) {
            Text("Розрахувати")
        }

        // Отображение результата
        Text(text = result, modifier = Modifier.padding(top = 16.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun EmissionCalculatorPreview() {
    Lab1Theme {
        EmissionCalculatorApp(onBackPressed = {})
    }
}
