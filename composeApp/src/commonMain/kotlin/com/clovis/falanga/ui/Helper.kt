package com.clovis.falanga.ui

import androidx.compose.runtime.Composable

@Composable
expect fun HandleBackPress(): Unit

expect fun isAndroid(): Boolean

@Composable
expect fun getContext(): Any