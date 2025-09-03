package com.clovis.falanga.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.clovis.falanga.ui.Screen
import com.clovis.falanga.ui.components.SplashImage
import kotlinx.coroutines.delay

@Composable
fun Splash(navController: NavHostController) {
    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        SplashImage()
    }

    LaunchedEffect(Unit) {
        delay(3000) // Delay for 3 seconds
        navController.navigate(Screen.CryptosRoute.route+"/crypto")
    }
}