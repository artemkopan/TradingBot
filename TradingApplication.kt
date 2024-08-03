package io.trading.bot

import android.app.Application
import android.util.Log
import io.trading.bot.mapper.MapperModule
import io.trading.bot.network.NetworkModule
import io.trading.bot.repo.RepoModule
import io.trading.bot.service.ServiceModule
import io.trading.bot.storage.StorageModule
import io.trading.bot.ui.UiModule
import io.trading.bot.usecase.UseCaseModule
import io.trading.bot.worker.LogMessageWorkerScheduler
import io.trading.bot.worker.WorkerModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module
import timber.log.Timber

class TradingApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
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
        Timber.plant(Timber.DebugTree(), ErrorReportingTree())
    }

    class ErrorReportingTree : Timber.Tree(), KoinComponent {

        private val messageScheduler by inject<LogMessageWorkerScheduler>()

        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            if (priority < Log.ERROR) return
            messageScheduler.schedule(
                """
                Error happened: $message
                Tag: $tag
                Throwable: $t
                Stacktrace: ${
                    (t ?: Exception().stackTrace.drop(4).take(3)
                        .joinToString { "${it.className}:${it.methodName}:${it.lineNumber}" })
                }
            """.trimIndent()
            )
        }
    }
}