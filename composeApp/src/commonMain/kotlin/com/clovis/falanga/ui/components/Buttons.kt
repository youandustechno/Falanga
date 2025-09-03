package com.clovis.falanga.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun ConfirmButton(buttonText: String, onClick: () -> Unit ) {
    AppButton(buttonText = buttonText, Color(0xFF8E7B72)) {
        onClick()
    }
}

@Composable
fun CloseButton(buttonText: String, onClick: () -> Unit ) {
    Button(
        onClick = { onClick() },
        colors = ButtonDefaults
            .buttonColors(Color(0xFF4A302B)),
        modifier = Modifier
            .wrapContentWidth()
            .height(48.dp)
            .padding(horizontal = 5.dp),
        elevation = ButtonDefaults.buttonElevation(4.dp)
    ) {
        Text(buttonText, color = Color.White)
    }
}

@Composable
fun DenyButton(buttonText: String, onClick: () -> Unit ) {
    AppButton(buttonText = buttonText, Color(0xFF4A302B)) {
        onClick()
    }
}

@Composable
fun AppButton(buttonText: String, color: Color, onClick: () -> Unit) {
    Button(
        onClick = { onClick() },
        colors = ButtonDefaults
            .buttonColors(color),
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 32.dp)
    ) {
        Text(buttonText, color = Color.White)
    }
}

@Composable
fun LinkButton(value: String, color: Color = Color(0XFF4A3125), click: () -> Unit) {
    Box(modifier = Modifier
        .wrapContentWidth()
        .defaultMinSize(minWidth = 30.dp, minHeight = 10.dp)
        .background(color, shape = RoundedCornerShape(10.dp))
        .clickable {
            click()
        }) {
        Text(value, color = Color.White,
            style =  MaterialTheme.typography.labelSmall,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(start = 9.dp, end = 9.dp, top = 5.dp, bottom = 5.dp))
    }
}

@Composable
fun CryptoButton(value: String, color: Color = Color(0XFF4A3125), click: () -> Unit) {
    Box(modifier = Modifier
        .wrapContentWidth()
        .defaultMinSize(minWidth = 40.dp, minHeight = 10.dp)
        .background(color, shape = RoundedCornerShape(10.dp))
        .padding(3.dp)
        .clickable {
            click()
        }) {
        Text(value, color = Color.White,
            style =  MaterialTheme.typography.labelSmall,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(start = 9.dp, end = 9.dp, top = 5.dp, bottom = 5.dp))
    }
}