package io.trading.bot.worker

import android.content.Context
import androidx.work.WorkManager
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module


@Module
@ComponentScan("io.trading.bot.worker")
class WorkerModule {

    @Factory
    fun provideWorkManager(context: Context) = WorkManager.getInstance(context)
}
