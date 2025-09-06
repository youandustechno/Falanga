package com.clovis.falanga.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.clovis.falanga.StringsUtil.EMPTY


@Composable
fun TextFieldWithIcons(value: String, icon: ImageVector, description: String, textChange:(String) -> Unit) {

    OutlinedTextField(
        value = value,
        trailingIcon = {
            Box(
                Modifier
                    .wrapContentSize()
                    .clickable {
                        try {
                            textChange(EMPTY)

                        } catch (e: Exception) {
                            textChange(EMPTY)
                        }

                    }) {
                Image(imageVector = icon, contentDescription = description)
            }
        },
        //trailingIcon = { Icon(imageVector = Icons.Default.Add, contentDescription = null) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        onValueChange = {
            textChange.invoke(it)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        label = { Text(text = description) },
        placeholder = { Text(text = description) },
    )
}

@Composable
fun PrecisionTextField(value: String, textChange:(String) -> Unit) {
    OutlinedTextField(
        value = value,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        onValueChange = {
            textChange.invoke(it)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        placeholder = { Text(text = "0") },
    )
}

