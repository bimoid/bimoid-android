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

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import io.github.bimoid.BimoidPreferences
import io.github.bimoid.cl.ContactListManager
import io.github.bimoid.pres.PresenceManager
import io.github.bimoid.service.BimoidService
import io.github.bimoid.ua.AvatarManager
import io.github.bimoid.ui.component.AuthForm
import io.github.bimoid.ui.component.ContactList
import io.github.bimoid.ui.component.TopBar
import io.github.bimoid.ui.theme.BimoidTheme
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Alexander Krysin
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var preferences: BimoidPreferences

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        if (preferences.getIsFirstLaunch()) {
            preferences.setIsFirstLaunch(false)
            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
        }
        lifecycleScope.launch {
            if (viewModel.getAccountsAsync().await().isNotEmpty()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(Intent(
                        this@MainActivity,
                        BimoidService::class.java
                    ))
                } else {
                    startService(Intent(
                        this@MainActivity,
                        BimoidService::class.java
                    ))
                }
            }
            viewModel.getLastMessages()
        }
        setContent {
            var actionsMenuExpanded by remember { mutableStateOf(false) }
            var contactList by remember { ContactListManager.contactList }
            val onlineUsers by remember { PresenceManager.onlineUsers }
            //val coroutineScope = rememberCoroutineScope()
            val lastMessages by remember { viewModel.lastMessages }
            val userAvatars by remember { AvatarManager.userAvatars }
            BimoidTheme {
                Scaffold(
                    modifier = Modifier.background(MaterialTheme.colorScheme.primary),
                    topBar = {
                        TopBar(
                            onMenuButtonClick = {
                                //coroutineScope.launch { scaffoldState.drawerState.open() }
                            },
                            onActionsButtonClick = { /*actionsMenuExpanded = true*/ },
                            actionsMenuExpanded = actionsMenuExpanded,
                            onActionsMenuDismissRequest = { /*actionsMenuExpanded = false*/ },
                            onDisconnectClick = {
                                stopService(
                                    Intent(
                                        this@MainActivity,
                                        BimoidService::class.java
                                    )
                                )
                            },
                            onLogoutClick = { viewModel.exitFromAccounts()/*; contactList = null*/ }
                        )
                    },
                    //drawerContent = { Drawer() },
                ) { contentPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = MaterialTheme.colorScheme.background)
                            .padding(contentPadding),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (contactList == null) {
                            AuthForm(viewModel) {
                                startService(Intent(this@MainActivity, BimoidService::class.java))
                            }
                        }
                        contactList?.let { contactList ->
                            ContactList(
                                contactList = contactList,
                                onlineUsers = onlineUsers,
                                onContactListItemClick = { itemId ->
                                    val intent = Intent(this@MainActivity, ChatActivity::class.java)
                                    intent.putExtra("contact_id", itemId)
                                    startActivity(intent)
                                },
                                lastMessages = lastMessages,
                                userAvatars = userAvatars
                            )
                        }
                    }
                }
            }
        }
    }
}