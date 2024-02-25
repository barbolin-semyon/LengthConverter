package ru.protei.lengthconverter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.protei.lengthconverter.ui.theme.LengthConverterTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LengthConverterTheme(true) {
                LengthConverterScreen()
            }
        }
    }
}

@Composable
fun LengthConverterScreen() {
    val viewModel: MainViewModel = viewModel()
    var inUnit by remember { mutableStateOf(LengthUnit.METER) }
    var outUnit by remember { mutableStateOf(LengthUnit.METER) }
    var inputValue by remember { mutableStateOf(0.0) }
    var outputValue by remember { mutableStateOf(0.0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LengthInputField(inUnit, inputValue) { unit, value ->
            inputValue = value
            outputValue = viewModel.convertLength(inputValue, inUnit, outUnit)
        }

        Row(
            modifier = Modifier.fillMaxWidth(0.8f).padding(vertical = 32.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LengthDropdown(inUnit) {
                inUnit = it
                outputValue = viewModel.convertLength(inputValue, inUnit, outUnit)
            }

            Text(
                text = "В",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            LengthDropdown(outUnit) {
                outUnit = it
                outputValue = viewModel.convertLength(inputValue, inUnit, outUnit)
            }
        }
        Text(
            "Конвертация: из ${inUnit.label} в ${outUnit.label}: $outputValue",
            color = MaterialTheme.colorScheme.onBackground
        )
        Button(onClick = {
            val temp = inUnit
            inUnit = outUnit
            outUnit = temp
            outputValue = viewModel.convertLength(inputValue, inUnit, outUnit)
        }) {
            Text("Поменять местами")
        }
    }
}

@Composable
fun LengthDropdown(selectedUnit: LengthUnit, onUnitSelected: (LengthUnit) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        Row(
            modifier = Modifier
                .border(1.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.medium)
                .padding(16.dp)
                .clickable { expanded = true },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = selectedUnit.label,
                color = MaterialTheme.colorScheme.primary,
            )

            Icon(
                tint = MaterialTheme.colorScheme.primary,
                painter = painterResource(id = R.drawable.baseline_arrow_drop_down_24),
                contentDescription = null,
            )
        }


        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(200.dp)
        ) {
            LengthUnit.values().forEach { unit ->
                DropdownMenuItem(
                    text = { Text(text = unit.label) },
                    onClick = {
                        onUnitSelected(unit)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LengthInputField(
    selectedUnit: LengthUnit,
    inputValue: Double,
    onValueChange: (LengthUnit, Double) -> Unit
) {
    var text by remember { mutableStateOf("") }

    Column {

        TextField(
            value = text,
            onValueChange = {
                text = it
                val newValue = it.toDoubleOrNull() ?: 0.0
                onValueChange(selectedUnit, newValue)
            },
            label = { Text("Enter value in ${selectedUnit.label}") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )
    }
}

