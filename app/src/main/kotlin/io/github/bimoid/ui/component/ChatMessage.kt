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

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.bimoid.R
import io.github.bimoid.data.entity.Message
import io.github.bimoid.data.entity.MessageDirection
import io.github.bimoid.ui.theme.BimoidTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * @author Alexander Krysin
 */
@Composable
fun ChatMessage(message: Message) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = if (message.direction == MessageDirection.INCOMING) {
            Arrangement.Start
        } else {
            Arrangement.End
        }
    ) {
        Card(
            backgroundColor = if (message.direction == MessageDirection.OUTGOING) {
                colorResource(id = R.color.primary)
            } else {
                MaterialTheme.colors.surface
            }
        ) {
            Column(
                modifier = Modifier
                    .requiredWidthIn(0.dp, 320.dp)
                    .wrapContentHeight()
                    .padding(12.dp),
                horizontalAlignment = if (message.direction == MessageDirection.INCOMING) {
                    Alignment.Start
                } else {
                    Alignment.End
                }
            ) {
                Text(text = message.text)
                Text(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .alpha(0.75f),
                    text = message.datetime.format(
                        DateTimeFormatter.ofPattern("HH:mm")
                    ),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun IncomingChatMessagePreview() {
    BimoidTheme {
        ChatMessage(
            Message(
                contact = "John Doe",
                direction = MessageDirection.INCOMING,
                text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                read = true,
                delivered = true,
                datetime = LocalDateTime.now()
            )
        )
    }
}

@Composable
@Preview(showBackground = true)
fun OutgoingChatMessagePreview() {
    BimoidTheme {
        ChatMessage(
            Message(
                contact = "Jane Doe",
                direction = MessageDirection.OUTGOING,
                text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                read = true,
                delivered = true,
                datetime = LocalDateTime.now()
            )
        )
    }
}