package com.clovis.falanga.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SplashImage() {
    Box( modifier = Modifier
        .size(150.dp)
        .graphicsLayer { shape = RoundedCornerShape(50.dp); clip = true }
        .background(Color.White, shape = CircleShape)
        .border(
            4.dp, Color(0xFF7F675B),
            shape = CircleShape
        ),
        contentAlignment = Alignment.Center ) {
        Text( text = "$",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF7F675B) )
    }
}