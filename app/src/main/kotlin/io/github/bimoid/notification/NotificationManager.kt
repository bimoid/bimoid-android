/*
 * Bimoid - Corporate messenger
 * Copyright (C) 2014—2022 Alexander Krysin
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.bimoid.notification

import android.content.ContentResolver.SCHEME_ANDROID_RESOURCE
import android.content.Context
import android.media.AudioAttributes
import android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION
import android.media.AudioAttributes.USAGE_NOTIFICATION
import android.media.AudioManager.STREAM_NOTIFICATION
import android.net.Uri
import android.os.Build
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.CATEGORY_MESSAGE
import androidx.core.app.NotificationCompat.PRIORITY_HIGH
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.NotificationManagerCompat.IMPORTANCE_HIGH
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.bimoid.R
import io.github.bimoid.ui.theme.primary
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author Alexander Krysin
 */
@Singleton
class NotificationManager @Inject constructor(@ApplicationContext private val context: Context) {
    private val notificationManager = NotificationManagerCompat.from(context)
    private var notificationId = 0
        get() = field++

    init {
        if (isNotificationChannelsSupported() && !isNotificationChannelExists()) {
            notificationManager.createNotificationChannel(
                NotificationChannelCompat.Builder(CHANNEL_ID, IMPORTANCE_HIGH)
                    .setName("Messages")
                    .setDescription("Bimoid messages")
                    .setLightsEnabled(true)
                    .setShowBadge(true)
                    .setSound(
                        Uri.parse(
                            SCHEME_ANDROID_RESOURCE + "://" + context.packageName + "/" + R.raw.snd_inc_msg
                        ),
                        AudioAttributes.Builder()
                            .setContentType(CONTENT_TYPE_SONIFICATION)
                            .setUsage(USAGE_NOTIFICATION)
                            .build()
                    )
                    .setVibrationEnabled(true)
                    .setVibrationPattern(longArrayOf(100, 300, 200, 300))
                    .build()
            )
        }
    }

    fun showMessageNotification(contactName: String, messageText: String) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setCategory(CATEGORY_MESSAGE)
            .setPriority(PRIORITY_HIGH)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.ic_bimoid)
            .setContentTitle(contactName)
            .setContentText(messageText)
            .addAction(R.drawable.ic_bimoid, "Прочитано", null)
            .setColorized(true)
            .setColor(primary.toArgb())
            .setSound(
                Uri.parse(
                    SCHEME_ANDROID_RESOURCE + "://" + context.packageName + "/" + R.raw.snd_inc_msg
                ),
                STREAM_NOTIFICATION
            )
            .setVibrate(longArrayOf(100, 300, 200, 300))
            .build()
        notificationManager.notify(notificationId, notification)
    }

    private fun isNotificationChannelsSupported() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

    private fun isNotificationChannelExists(): Boolean {
        return notificationManager.getNotificationChannelCompat(CHANNEL_ID) != null
    }

    companion object {
        const val CHANNEL_ID = "io.github.bimoid.messages"
    }
}