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

package io.github.bimoid.pres

import androidx.compose.runtime.mutableStateOf
import io.github.bimoid.ua.AvatarManager
import io.github.obimp.listener.PresenceInfoListener
import io.github.obimp.presence.MailNotification
import io.github.obimp.presence.OnlineContactInfo
import io.github.obimp.presence.PresenceInfo
import io.github.obimp.presence.PresenceInfoParameters

/**
 * @author Alexander Krysin
 */
object PresenceManager : PresenceInfoListener {
    val onlineUsers = mutableStateOf<List<String>>(listOf())

    override fun onContactOffline(accountName: String) {
        onlineUsers.value -= accountName
    }

    override fun onContactOnline(onlineContactInfo: OnlineContactInfo) {
        onlineUsers.value += onlineContactInfo.accountName
        onlineContactInfo.avatarMD5Hash?.let {
            AvatarManager.requestAvatar(it, onlineContactInfo.accountName)
        }
    }

    override fun onMailNotification(mailNotification: MailNotification) {}

    override fun onOwnMailURL(ownMailURL: String) {}

    override fun onPresenceInfo(presenceInfo: PresenceInfo) {}

    override fun onPresenceInfoParameters(presenceInfoParameters: PresenceInfoParameters) {}
}