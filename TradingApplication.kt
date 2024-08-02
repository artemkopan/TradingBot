package io.trading.bot

import android.app.Application
import io.trading.bot.mapper.MapperModule
import io.trading.bot.network.NetworkModule
import io.trading.bot.repo.RepoModule
import io.trading.bot.service.ServiceModule
import io.trading.bot.storage.StorageModule
import io.trading.bot.ui.UiModule
import io.trading.bot.usecase.UseCaseModule
import io.trading.bot.worker.WorkerModule
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.logger.Logger
import org.koin.core.logger.MESSAGE
import org.koin.ksp.generated.module
import timber.log.Timber

class TradingApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        startKoin {
            logger(object : Logger() {
                override fun display(level: Level, msg: MESSAGE) {
                    Timber.tag("Koin").d(msg)
                }
            })
            androidContext(this@TradingApplication)
            workManagerFactory()
            modules(
                ServiceModule().module,
                WorkerModule().module,
                NetworkModule().module,
                UiModule().module,
                RepoModule().module,
                StorageModule().module,
                UseCaseModule().module,
                MapperModule().module
            )
        }
    }
}