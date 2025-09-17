package com.appfolks.mtgedhmatchtracker.domain.model

import androidx.compose.ui.graphics.Color

enum class CommanderColor(val color: Color, val textColor: Color) {
    WHITE(Color(0xFFFFFBD5), Color.Black),
    BLUE(Color(0xFF0E68AB), Color.White),
    BLACK(Color(0xFF150B00), Color.White),
    RED(Color(0xFFD3202A), Color.White),
    GREEN(Color(0xFF00733E), Color.White),
    COLORLESS(Color(0xFFCAC5C0), Color.Black);
}
