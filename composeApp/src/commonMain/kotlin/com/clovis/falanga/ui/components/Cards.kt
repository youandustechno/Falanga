package com.clovis.falanga.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun CryptoCard(content: @Composable () -> Unit) {
    Card {
        Column(
            Modifier
                .fillMaxWidth()
                .wrapContentHeight()) {
            content()
        }
    }
}