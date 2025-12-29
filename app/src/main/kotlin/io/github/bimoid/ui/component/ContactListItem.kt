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

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.bimoid.data.entity.Message
import io.github.bimoid.ui.theme.BimoidTheme
import io.github.obimp.cl.Contact
import java.nio.ByteBuffer

/**
 * @author Alexander Krysin
 */

@Composable
fun ContactListItem(
    contact: Contact? = null,
    isOnline: Boolean,
    onClick: (Int) -> Unit = {},
    lastMessage: Message? = null,
    userAvatar: ByteBuffer? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { contact?.run { onClick(id) } },
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(color = MaterialTheme.colorScheme.primary, shape = CircleShape)
        ) {
            userAvatar?.let {
                Image(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .align(Alignment.Center),
                    bitmap = BitmapFactory.decodeByteArray(it.array(), 0, it.remaining()).asImageBitmap(),
                    contentDescription = "User avatar"
                )
            }
            if (userAvatar == null) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = if (contact?.contactName != null) contact.contactName.first()
                        .uppercase() else "А",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            if (isOnline) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 34.dp, top = 34.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(
                                color = Color(0xFF00BB00),
                                shape = CircleShape
                            )
                    )
                }
            }
        }
        Column {
            Text(
                text = contact?.contactName ?: "Alex",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Text(
                text = lastMessage?.text ?: "Нет сообщений",
                color = Color.Gray,
                fontSize = 18.sp,
                fontStyle = if (lastMessage == null) FontStyle.Italic else FontStyle.Normal,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContactListItemPreview() {
    BimoidTheme {
        ContactListItem(isOnline = true)
    }
}