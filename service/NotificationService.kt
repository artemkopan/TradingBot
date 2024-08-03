package io.trading.bot.service

import android.app.Notification
import android.os.Bundle
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import io.trading.bot.usecase.notification.HandleNotificationUseCase
import org.koin.android.ext.android.get
import timber.log.Timber
import io.trading.bot.model.Notification as ParsedNotification


class NotificationService : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        Timber.tag(TAG).d("onNotificationPosted() called with: sbn = %s", sbn)
        val extras: Bundle = sbn.notification.extras
        Timber.tag(TAG)
            .d("extras: ${extras.keySet().joinToString { "ket [$it] = ${extras.get(it)}" }}")

        val title = extras.getString(Notification.EXTRA_TITLE)
        val text = extras.getCharSequence(Notification.EXTRA_TEXT)
        val bigText = extras.getCharSequence(Notification.EXTRA_BIG_TEXT)
        val subText = extras.getCharSequence(Notification.EXTRA_SUB_TEXT)

        Timber.tag(TAG).d("Notification: $title, text: $text, bigText: $bigText, subText: $subText")
        logActiveNotifications()

        val notification = ParsedNotification(
            packageName = sbn.packageName, title = title.toString(), text = text.toString()
        )

        get<HandleNotificationUseCase>().invoke(notification)
    }

    private fun logActiveNotifications() {
        val activeNotifications = activeNotifications

        for (sbn in activeNotifications) {
            val notification = sbn.notification
            val extras: Bundle = notification.extras

            val title = extras.getString(Notification.EXTRA_TITLE)
            val text = extras.getCharSequence(Notification.EXTRA_TEXT)
            val bigText = extras.getCharSequence(Notification.EXTRA_BIG_TEXT)

            Timber.tag(TAG).i("Notification Package: %s", sbn.packageName)
            Timber.tag(TAG).i("Notification ID: %s", sbn.id)
            Timber.tag(TAG).i("Notification Title: %s", title)
            Timber.tag(TAG).i("Notification Text: %s", text)

            if (!bigText.isNullOrEmpty()) {
                Timber.tag(TAG).i("Notification Big Text: %s", bigText)
            }
        }
    }

    companion object {
        private const val TAG = "NotificationService"
    }
}