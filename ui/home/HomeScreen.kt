package io.trading.bot.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.trading.bot.ui.common.Screen
import io.trading.bot.ui.common.ScreenProvider
import io.trading.bot.ui.signin.SignInScreenProvider
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single
@Named("home")
data class HomeScreenProvider(
    override val builder: NavGraphBuilder.() -> Unit = { composable<HomeScreen> { HomeScreenContent() } }
) : ScreenProvider {
    @Serializable
    data object HomeScreen : Screen
}

@Composable
fun HomeScreenContent(viewModel: HomeViewModel = koinViewModel()) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { viewModel.openNotificationService() }) {
                Text("Grant permissions")
            }
            Button(
                onClick = { viewModel.navigateTo(SignInScreenProvider.SignInScreen) }) {
                Text("Sign-In")
            }
            Button(
                onClick = { viewModel.getQuotes() }) {
                Text("Get Quotes")
            }
        }
    }
}