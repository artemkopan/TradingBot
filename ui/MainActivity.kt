package io.trading.bot.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import io.trading.bot.ui.common.Router
import io.trading.bot.ui.common.ScreenProvider
import io.trading.bot.ui.home.HomeScreenProvider
import io.trading.bot.ui.theme.TradingBotTheme
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.android.inject


class MainActivity : ComponentActivity() {

    private val router: Router by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TradingBotTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = HomeScreenProvider.HomeScreen
                ) {
                    getKoin().getAll<ScreenProvider>().forEach { provider ->
                        provider.builder(this)
                    }
                }
                LaunchedEffect(true) {
                    router.navigationFlow.collect { navController.navigate(it) }
                }
            }
        }
    }
}
