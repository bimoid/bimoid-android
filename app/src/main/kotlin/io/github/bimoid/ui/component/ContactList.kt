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

package io.github.bimoid.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.bimoid.data.entity.Message
import io.github.obimp.cl.Contact
import io.github.obimp.cl.ContactListItem
import java.nio.ByteBuffer

/**
 * @author Alexander Krysin
 */

@Composable
fun ContactList(
    contactList: List<ContactListItem>,
    onlineUsers: List<String>,
    onContactListItemClick: (Int) -> Unit,
    lastMessages: Map<String, Message>? = null,
    userAvatars: Map<String, ByteBuffer> = mapOf()
) {
    var isFirstItem = true
    Column(modifier = Modifier.navigationBarsPadding()) {
        contactList.filterIsInstance<Contact>().forEach {
            if (!isFirstItem) {
                Divider(startIndent = 72.dp)
            }
            isFirstItem = false
            ContactListItem(
                it,
                onlineUsers.contains(it.accountName),
                onContactListItemClick,
                lastMessages?.get(it.accountName),
                userAvatars[it.accountName]
            )
        }
    }
}