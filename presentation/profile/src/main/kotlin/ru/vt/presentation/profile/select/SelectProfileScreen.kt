package ru.vt.presentation.profile.select

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.insets.systemBarsPadding

@Composable
fun SelectProfileScreen() {
    Surface(modifier = Modifier
        .fillMaxSize()
        .systemBarsPadding(true)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Text("Select Profile Screen")
            }
        }
    }
}