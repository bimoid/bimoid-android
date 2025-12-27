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

package io.github.bimoid.ua

import androidx.compose.runtime.mutableStateOf
import io.github.bimoid.connection.ConnectionManager
import io.github.obimp.listener.UserAvatarsListener
import io.github.obimp.ua.AvatarResult
import io.github.obimp.ua.AvatarSetResult
import io.github.obimp.ua.UserAvatarsParameters
import java.nio.ByteBuffer

/**
 * @author Alexander Krysin
 */
object AvatarManager : UserAvatarsListener {
    private val avatarMd5HashToAccountName = mutableMapOf<ByteArray, String>()
    private val avatars = mutableMapOf<String, ByteBuffer>()
    val userAvatars = mutableStateOf(avatars)

    override fun userAvatarsParameters(userAvatarsParameters: UserAvatarsParameters) {}

    override fun onAvatar(avatarResult: AvatarResult) {
        if (avatarResult.avatar != null) {
            avatars[avatarMd5HashToAccountName.getOrDefault(avatarResult.avatarMD5Hash, "")] =
                avatarResult.avatar!!
        }
    }

    override fun onSetAvatar(avatarSetResult: AvatarSetResult) {}

    fun requestAvatar(avatarMd5Hash: ByteArray, accountName: String) {
        avatarMd5HashToAccountName.computeIfAbsent(avatarMd5Hash) { accountName }
        ConnectionManager.connections.keys.first().loadAvatar(avatarMd5Hash)
    }
}