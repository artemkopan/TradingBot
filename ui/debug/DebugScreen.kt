@file:OptIn(ExperimentalLayoutApi::class)

package io.trading.bot.ui.debug

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.trading.bot.ui.common.Screen
import io.trading.bot.ui.common.ScreenProvider
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single
@Named("debug")
data class DebugScreenProvider(
    override val builder: NavGraphBuilder.() -> Unit = { composable<DebugScreen> { DebugScreenContent() } }
) : ScreenProvider {
    @Serializable
    data object DebugScreen : Screen
}

@Composable
fun DebugScreenContent(viewModel: DebugViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .imePadding()
                .imeNestedScroll()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(state.status, modifier = Modifier.padding(top = 16.dp))

            Button(
                onClick = { viewModel.getQuotes() }) {
                Text("Get Quotes")
            }
            Button(
                onClick = { viewModel.sendRaiseNotification() }) {
                Text("Send Raise Notification")
            }
            Button(
                onClick = { viewModel.sendBelowNotification() }) {
                Text("Send Below Notification")
            }
            Button(
                onClick = { viewModel.sendUnhandledNotification() }) {
                Text("Send Unhandled Notification")
            }
            if (state.loading) {
                CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
            }
        }
    }
}
