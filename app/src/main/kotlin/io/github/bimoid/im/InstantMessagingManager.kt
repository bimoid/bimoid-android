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

package io.github.bimoid.im

import io.github.bimoid.notification.NotificationManager
import io.github.obimp.listener.MessageListener
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author Alexander Krysin
 */
@Singleton
class InstantMessagingManager @Inject constructor(
    private val notificationManager: NotificationManager
) : MessageListener {
    override fun onIncomingMessage(
        accountName: String,
        messageId: Int,
        messageType: Int,
        messageData: String
    ) {
        notificationManager.showMessageNotification(accountName, messageData)
    }

    override fun onMessageDelivered(accountName: String, messageId: Int) {}

    override fun onNotify(accountName: String, notificationType: Int, notificationValue: Int) {}
}