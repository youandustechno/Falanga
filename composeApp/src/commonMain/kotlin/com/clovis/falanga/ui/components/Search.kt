package com.clovis.falanga.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import falanga.composeapp.generated.resources.Res
import falanga.composeapp.generated.resources.close_img
import falanga.composeapp.generated.resources.name_img
import org.jetbrains.compose.resources.imageResource

@Composable
fun SearchView(state: String, onChange:(String) -> Unit) {

    CryptoCard {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, true),
                value = state,
                onValueChange = { value ->
                    onChange(value)
                },
                textStyle = MaterialTheme
                    .typography
                    .bodyLarge
                    .copy(

                    ),
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
//                    textColor = Color.Gray,
//                    disabledTextColor = Color.Transparent,
//                    backgroundColor = Color.White,
//                    focusedIndicatorColor = Color.Transparent,
//                    unfocusedIndicatorColor = Color.Transparent,
//                    disabledIndicatorColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.width(5.dp))
           // Image(imageResource( Res.drawable.close_img), contentDescription = "Search")
        }
    }
}