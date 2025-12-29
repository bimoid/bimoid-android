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

package io.github.bimoid.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import io.github.bimoid.cl.ContactListManager
import io.github.bimoid.ui.component.ChatScreen
import io.github.bimoid.ui.theme.BimoidTheme
import io.github.obimp.cl.Contact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @author Alexander Krysin
 */
@AndroidEntryPoint
class ChatActivity : ComponentActivity() {
    private val viewModel: ChatViewModel by viewModels()

    //@Inject
    //lateinit var database: BimoidDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val contact = ContactListManager.contactList.value!!.find {
            it.id == intent.extras!!.getInt("contact_id")
        } as Contact
        lifecycleScope.launch {
            val messages = viewModel.getMessagesForContactAsync(contact.accountName).await()
            setContent {
                BimoidTheme {
                    ChatScreen(
                        contact = contact,
                        messages = messages,
                        onBackPressed = onBackPressedDispatcher::onBackPressed,
                        onSendMessage = { _ ->
                            lifecycleScope.launch(Dispatchers.IO) {
                                /*OBIMP.sendMessage(
                                    ConnectionManager.connections.keys.first(),
                                    contact.accountName,
                                    text
                                )
                                database.messageDao().insert(
                                    Message(
                                        contact = contact.accountName,
                                        direction = MessageDirection.OUTGOING,
                                        text = text,
                                        read = true,
                                        delivered = false,
                                        datetime = LocalDateTime.now()
                                    )
                                )*/
                            }
                        }
                    )
                }
            }
        }
    }
}