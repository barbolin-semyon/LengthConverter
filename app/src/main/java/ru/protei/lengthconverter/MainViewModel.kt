package ru.protei.lengthconverter

import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    fun convertLength(value: Double, fromUnit: LengthUnit, toUnit: LengthUnit): Double {
        val valueInMeters = value * fromUnit.valueRelativeToMeter
        return valueInMeters / toUnit.valueRelativeToMeter
    }
}

enum class LengthUnit(val label: String, val valueRelativeToMeter: Double) {
    MILLIMETER("миллиметры", 0.001),
    CENTIMETER("сантиметры", 0.01),
    METER("метры", 1.0),
    KILOMETER("километры", 1000.0),
    INCH("дюймы", 0.0254),
    FOOT("футы", 0.3048),
    YARD("ярды", 0.9144),
    MILE("мили", 1609.344),
    NAUTICAL_MILE("морские мили", 1852.0),
    LIGHT_YEAR("световые годы", 9.461e15)
}
