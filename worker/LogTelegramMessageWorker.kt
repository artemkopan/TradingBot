package io.trading.bot.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import io.trading.bot.model.Notification
import io.trading.bot.network.telegram.TelegramNetwork
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Factory
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

private const val KEY_MESSAGE = "message"

@Factory
class LogTelegramMessageWorkerScheduler(private val workManager: WorkManager) {

    fun schedule(notification: Notification) {
        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

        val request = OneTimeWorkRequestBuilder<LogTelegramMessageWorker>()
            .setConstraints(constraints)
            .setInputData(workDataOf(KEY_MESSAGE to Json.encodeToString(notification)))
            .build()

        workManager.enqueue(request)
    }
}

class LogTelegramMessageWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params), KoinComponent {

    override suspend fun doWork(): Result {
        val message = requireNotNull(inputData.getString(KEY_MESSAGE)?.ifBlank { null }) {
            "Message is empty empty"
        }
        get<TelegramNetwork>().sendMessage(message)
        return Result.success()
    }
}