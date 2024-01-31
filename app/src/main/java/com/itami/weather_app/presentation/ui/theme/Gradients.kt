package com.itami.weather_app.presentation.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

data class Gradient(
    val primaryGradient: Brush,
    val secondaryGradient: Brush,
    val shadowColor: Color,
    val onGradients: Color,
) {
    constructor(
        firstColor: Color,
        secondColor: Color,
        thirdColor: Color,
        fourthColor: Color,
        onGradient: Color = Color.White,
    ) : this(
        primaryGradient = Brush.linearGradient(listOf(firstColor, secondColor)),
        secondaryGradient = Brush.linearGradient(listOf(thirdColor, fourthColor)),
        shadowColor = firstColor,
        onGradients = onGradient
    )
}

@Immutable
data class Gradients(
    val orange: Gradient = Gradient(
        firstColor = Color(0xFFFFDF37),
        secondColor = Color(0xFFFF5621),
        thirdColor = Color(0xFFFFCE21),
        fourthColor = Color(0xFFFF7F57),
    ),
    val purple: Gradient = Gradient(
        firstColor = Color(0xFFFB37FF),
        secondColor = Color(0xFF3531FF),
        thirdColor = Color(0x245000FF),
        fourthColor = Color(0x8A8433FF),
    ),
    val green: Gradient = Gradient(
        firstColor = Color(0xFF5CFF63),
        secondColor = Color(0xFF25FFFF),
        thirdColor = Color(0x1600B498),
        fourthColor = Color(0x00029A14),
    ),
    val blue: Gradient = Gradient(
        firstColor = Color(0xFF379FFF),
        secondColor = Color(0xFF4021FF),
        thirdColor = Color(0x633B67FF),
        fourthColor = Color(0x523895FF),
    ),
    val pink: Gradient = Gradient(
        firstColor = Color(0xFFFF7AA2),
        secondColor = Color(0xFF9021FF),
        thirdColor = Color(0xFFB33DE2),
        fourthColor = Color(0x00F270AD),
    ),
) {
    fun asList(): List<Gradient> = listOf(
        orange,
        purple,
        green,
        blue,
        pink
    )
}