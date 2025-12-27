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

import io.github.bimoid.BimoidDatabase
import io.github.bimoid.data.entity.Message
import io.github.bimoid.data.entity.MessageDirection
import io.github.bimoid.notification.NotificationManager
import io.github.obimp.im.EncryptionKeyReply
import io.github.obimp.im.IncomingMessage
import io.github.obimp.im.InstantMessagingParameters
import io.github.obimp.im.Notification
import io.github.obimp.listener.InstantMessagingListener
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author Alexander Krysin
 */
@Singleton
class InstantMessagingManager @Inject constructor(
    private val database: BimoidDatabase,
    private val notificationManager: NotificationManager
) : InstantMessagingListener {
    override fun onEncryptionKeyReply(
        accountName: String,
        encryptionKeyReply: EncryptionKeyReply
    ) {}

    override fun onEncryptionKeyRequest(accountName: String) {}

    override fun onIncomingMessage(incomingMessage: IncomingMessage) {
        val accountName = incomingMessage.accountName
        val text = String(incomingMessage.messageData.array())
        database.messageDao().insert(
            Message(
                contact = accountName,
                direction = MessageDirection.INCOMING,
                text = text,
                read = false,
                delivered = true,
                datetime = LocalDateTime.now()
            )
        )
        notificationManager.showMessageNotification(accountName, text)
    }

    override fun onInstantMessagingParameters(
        instantMessagingParameters: InstantMessagingParameters
    ) {}

    override fun onMessageReport(accountName: String, messageId: Int) {}

    override fun onNotify(notification: Notification) {}

    override fun onOfflineDone() {}
}