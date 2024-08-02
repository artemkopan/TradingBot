package io.trading.bot.service

import android.content.Context
import android.content.Intent
import org.koin.core.annotation.Factory

@Factory
class IntentLauncher(private val context: Context) {

    fun openNotificationSettings() {
        val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}