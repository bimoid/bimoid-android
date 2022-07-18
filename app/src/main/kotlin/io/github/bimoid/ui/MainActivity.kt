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

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import io.github.bimoid.BimoidPreferences
import io.github.bimoid.R
import io.github.bimoid.WelcomeActivity
import io.github.bimoid.cl.ContactListManager
import io.github.bimoid.pres.PresenceManager
import io.github.bimoid.service.BimoidService
import io.github.bimoid.ui.theme.BimoidTheme
import io.github.obimp.cl.Contact
import javax.inject.Inject

/**
 * @author Alexander Krysin
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var preferences: BimoidPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        if (preferences.getIsFirstLaunch()) {
            preferences.setIsFirstLaunch(false)
            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
        }
        startService(Intent(this, BimoidService::class.java))
        setContent {
            var actionsMenuExpanded by remember { mutableStateOf(false) }
            var contactList by remember { ContactListManager.contactList }
            val onlineUsers by remember { PresenceManager.onlineUsers }
            BimoidTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(text = stringResource(id = R.string.app_name))
                            },
                            actions = {
                                IconButton(onClick = { actionsMenuExpanded = true }) {
                                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Действия")
                                    DropdownMenu(expanded = actionsMenuExpanded, onDismissRequest = { actionsMenuExpanded = false }) {
                                        DropdownMenuItem(onClick = { stopService(Intent(this@MainActivity, BimoidService::class.java)) }) {
                                            Text(text = "Отключиться")
                                        }
                                        DropdownMenuItem(onClick = { viewModel.exitFromAccounts(); contactList = null }) {
                                            Text(text = "Выйти из аккаунта")
                                        }
                                    }
                                }
                            }
                        )
                    }
                ) { contentPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(contentPadding),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (contactList == null) {
                            AuthForm(viewModel)
                        }
                        contactList?.let { cl ->
                            var isFirstItem = true
                            cl.filterIsInstance<Contact>().forEach {
                                if (!isFirstItem) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(1.dp)
                                            .padding(start = 72.dp)
                                            .background(color = Color(0x11000000))
                                    )
                                }
                                isFirstItem = false
                                ContactListItem(it, onlineUsers.contains(it.accountName))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AuthForm(viewModel: MainViewModel? = null) {
    var server by remember { mutableStateOf("bimoid.net") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(elevation = 2.dp) {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
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
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = MaterialTheme.colors.surface
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )
                TextField(
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
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = MaterialTheme.colors.surface
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )
                TextField(
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
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = MaterialTheme.colors.surface
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )
                Button(onClick = {
                    viewModel?.addAccount(server, username, password)
                }) {
                    Text(text = "Войти")
                }
            }
        }
    }
}

@Composable
fun ContactListItem(contact: Contact? = null, isOnline: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(color = MaterialTheme.colors.primary, shape = CircleShape)
        ) {
            Text(
                text = if (contact?.contactName != null) contact.contactName.first().uppercase() else "А",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
            if (isOnline) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 32.dp, top = 32.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
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
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "@${contact?.accountName ?: "alex"}",
                fontSize = 18.sp,
                color = Color.Gray,
                fontStyle = FontStyle.Italic
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

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun DefaultPreview() {
    BimoidTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = stringResource(id = R.string.app_name))
                    },
                    actions = {
                        IconButton(onClick = {}) {
                            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Действия")
                        }
                    }
                )
            }
        ) { contentPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AuthForm()
            }
        }
    }
}