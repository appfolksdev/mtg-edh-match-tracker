package com.appfolks.mtgedhmatchtracker

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.appfolks.mtgedhmatchtracker.store.MatchScreenStoreProvider
import com.appfolks.mtgedhmatchtracker.ui.screen.MatchScreen

@Composable
@Preview
fun App() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val storeProvider = remember { MatchScreenStoreProvider() }
            val store = remember { storeProvider.provide() }
            
            DisposableEffect(storeProvider) {
                onDispose {
                    storeProvider.dispose()
                }
            }
            
            MatchScreen(
                store = store,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}