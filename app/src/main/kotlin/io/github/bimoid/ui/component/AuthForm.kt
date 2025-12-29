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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import io.github.bimoid.ui.MainViewModel
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.launch

/**
 * @author Alexander Krysin
 */

@Composable
fun AuthForm(viewModel: MainViewModel? = null, onAccountAdded: () -> Unit = {}) {
    var server by remember { mutableStateOf("bimoid.net") }
    var port by remember { mutableIntStateOf(7023) }
    var secure by remember { mutableStateOf(false) }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .imePadding()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Card(elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
            Column(
                modifier = Modifier
                    .width(300.dp)
                    .wrapContentHeight()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val serverBringIntoViewRequester = remember { BringIntoViewRequester() }
                TextField(
                    modifier = Modifier
                        .bringIntoViewRequester(serverBringIntoViewRequester)
                        .onFocusEvent {
                            if (it.isFocused) {
                                coroutineScope.launch {
                                    awaitFrame()
                                    serverBringIntoViewRequester.bringIntoView()
                                }
                            }
                        },
                    value = server,
                    onValueChange = { server = it },
                    label = { Text(text = "Сервер:") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Dns,
                            contentDescription = "Server"
                        )
                    },
                    singleLine = true,
                    maxLines = 1,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )
                val portBringIntoViewRequester = remember { BringIntoViewRequester() }
                TextField(
                    modifier = Modifier
                        .bringIntoViewRequester(portBringIntoViewRequester)
                        .onFocusEvent {
                            if (it.isFocused) {
                                coroutineScope.launch {
                                    awaitFrame()
                                    portBringIntoViewRequester.bringIntoView()
                                }
                            }
                        },
                    value = port.toString(),
                    onValueChange = { port = if (it.isNotBlank()) it.toInt() else 0 },
                    label = { Text(text = "Порт:") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Pin,
                            contentDescription = "Port"
                        )
                    },
                    singleLine = true,
                    maxLines = 1,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Number
                    )
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 14.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Secure",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.54f)
                    )
                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = "Шифрование",
                        color = LocalContentColor.current.copy(LocalContentColor.current.alpha)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Switch(
                            checked = secure,
                            onCheckedChange = { secure = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colorScheme.primary)
                        )
                    }
                }
                HorizontalDivider(color = Color.Gray, thickness = 0.75.dp)
                val usernameBringIntoViewRequester = remember { BringIntoViewRequester() }
                TextField(
                    modifier = Modifier
                        .bringIntoViewRequester(usernameBringIntoViewRequester)
                        .onFocusEvent {
                            if (it.isFocused) {
                                coroutineScope.launch {
                                    awaitFrame()
                                    usernameBringIntoViewRequester.bringIntoView()
                                }
                            }
                        },
                    value = username,
                    onValueChange = { username = it },
                    label = { Text(text = "ID Пользователя:") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "User"
                        )
                    },
                    singleLine = true,
                    maxLines = 1,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )
                val focusManager = LocalFocusManager.current
                val passwordBringIntoViewRequester = remember { BringIntoViewRequester() }
                TextField(
                    modifier = Modifier
                        .bringIntoViewRequester(passwordBringIntoViewRequester)
                        .onFocusEvent {
                            if (it.isFocused) {
                                coroutineScope.launch {
                                    awaitFrame()
                                    passwordBringIntoViewRequester.bringIntoView()
                                }
                            }
                        },
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(text = "Пароль:") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Password,
                            contentDescription = "Password"
                        )
                    },
                    singleLine = true,
                    maxLines = 1,
                    visualTransformation = PasswordVisualTransformation(),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                )
                Button(
                    onClick = {
                        viewModel?.addAccount(server, port, secure, username, password)
                        onAccountAdded()
                    },
                    enabled = server.isNotBlank() && port > 0 && username.isNotBlank() && password.isNotBlank()
                ) {
                    Text(text = "Войти")
                }
            }
        }
    }
}