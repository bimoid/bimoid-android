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

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import io.github.bimoid.data.entity.Message
import io.github.bimoid.data.entity.MessageDirection.INCOMING
import io.github.bimoid.data.entity.MessageDirection.OUTGOING
import java.time.LocalDateTime
import kotlin.random.Random
import kotlin.random.nextInt

class MessageListPreviewParameterProvider : PreviewParameterProvider<List<Message>> {
    override val values: Sequence<List<Message>>
        get() = sequence {
            val messages = mutableListOf<Message>()
            val count = Random.nextInt(3..10)
            for (i in 0..count) {
                messages.add(
                    Message(
                        id = i,
                        contact = if (Random.nextBoolean()) "John Doe" else "Jane Doe",
                        direction = if (Random.nextBoolean()) INCOMING else OUTGOING,
                        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                        read = Random.nextBoolean(),
                        delivered = Random.nextBoolean(),
                        datetime = LocalDateTime.now()
                    )
                )
            }
            yield(messages)
        }

}
