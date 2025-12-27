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

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.bimoid.R
import io.github.bimoid.data.entity.Message
import io.github.bimoid.ui.theme.BimoidTheme
import io.github.obimp.cl.Contact
import io.github.obimp.cl.PrivacyType

/**
 * @author Alexander Krysin
 */
@Composable
fun ChatScreen(
    contact: Contact,
    messages: List<Message>,
    onBackPressed: () -> Unit = {},
    onSendMessage: (String) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = colorResource(id = R.color.primary),
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                title = {
                    Row(verticalAlignment = Alignment.Top) {
                        Image(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape),
                            painter = painterResource(id = R.mipmap.ic_bimoid),
                            contentDescription = "Avatar"
                        )
                        Column(
                            modifier = Modifier.padding(start = 8.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = contact.contactName,
                                fontSize = 16.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                modifier = Modifier.alpha(0.75f),
                                text = "@${contact.accountName}",
                                fontSize = 16.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Chat options"
                        )
                    }
                }
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f, true)
                    .fillMaxWidth()
                    .wrapContentHeight(align = Alignment.Top)
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items = messages, key = Message::id) { message -> ChatMessage(message) }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                var messageText by remember { mutableStateOf("") }
                TextField(
                    modifier = Modifier.weight(1f, true),
                    value = messageText,
                    onValueChange = { messageText = it },
                    placeholder = { Text(text = "Текст соообщения...") },
                    maxLines = 3,
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent)
                )
                IconButton(
                    onClick = {
                        onSendMessage(messageText)
                        messageText = ""
                    },
                    enabled = messageText.isNotBlank()
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send message",
                        tint = colorResource(id = R.color.primary)
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun ChatScreenPreview(
    @PreviewParameter(MessageListPreviewParameterProvider::class, 1) messages: List<Message>
) {
    BimoidTheme {
        ChatScreen(
            Contact(
                id = 0,
                groupID = 0,
                accountName = "username",
                contactName = "Username",
                privacyType = PrivacyType.NONE,
                authorizationFlag = false,
                generalItemFlag = false,
                transportItemId = null
            ),
            messages
        )
    }
}