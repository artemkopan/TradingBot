package io.trading.bot.service

import android.app.Notification
import android.os.Bundle
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import io.trading.bot.worker.LogTelegramMessageWorkerScheduler
import org.koin.android.ext.android.get
import io.trading.bot.model.Notification as ParsedNotification


class NotificationService : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        Log.d(TAG, "onNotificationPosted() called with: sbn = $sbn")
        val extras: Bundle = sbn.notification.extras
        Log.d(TAG, "extras: ${extras.keySet().joinToString { "ket [$it] = ${extras.get(it)}" }}")

        val title = extras.getString(Notification.EXTRA_TITLE)
        val text = extras.getCharSequence(Notification.EXTRA_TEXT)
        val bigText = extras.getCharSequence(Notification.EXTRA_BIG_TEXT)
        val subText = extras.getCharSequence(Notification.EXTRA_SUB_TEXT)

        Log.d(TAG, "Notification: $title, text: $text, bigText: $bigText, subText: $subText")
        logActiveNotifications()

        val notification = ParsedNotification(
            packageName = sbn.packageName, title = title.toString(), text = text.toString()
        )

        get<LogTelegramMessageWorkerScheduler>().schedule(notification)
    }

    private fun logActiveNotifications() {
        val activeNotifications = activeNotifications

        for (sbn in activeNotifications) {
            val notification = sbn.notification
            val extras: Bundle = notification.extras

            val title = extras.getString(Notification.EXTRA_TITLE)
            val text = extras.getCharSequence(Notification.EXTRA_TEXT)
            val bigText = extras.getCharSequence(Notification.EXTRA_BIG_TEXT)

            Log.i(TAG, "Notification Package: ${sbn.packageName}")
            Log.i(TAG, "Notification ID: ${sbn.id}")
            Log.i(TAG, "Notification Title: $title")
            Log.i(TAG, "Notification Text: $text")

            if (!bigText.isNullOrEmpty()) {
                Log.i(TAG, "Notification Big Text: $bigText")
            }
        }
    }

    companion object {
        private const val TAG = "NotificationService"
    }
}