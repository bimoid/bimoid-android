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

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.bimoid.BimoidDatabase
import io.github.bimoid.data.entity.Account
import io.github.bimoid.data.entity.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.stream.Collectors
import javax.inject.Inject

/**
 * @author Alexander Krysin
 */
@HiltViewModel
class MainViewModel @Inject constructor(private val database: BimoidDatabase) : ViewModel() {
    val lastMessages = mutableStateOf<Map<String, Message>?>(null)

    fun addAccount(server: String, port: Int, secure: Boolean, username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            database.accountDao().insert(
                    Account(
                        server = server,
                        port = port,
                        secure = secure,
                        username = username,
                        password = password
                    )
                )
        }
    }

    fun exitFromAccounts() {
        viewModelScope.launch(Dispatchers.IO) {
            database.accountDao().getAll().forEach(database.accountDao()::delete)
        }
    }

    fun getAccountsAsync() = viewModelScope.async(Dispatchers.IO) {
        database.accountDao().getAll()
    }

    fun getLastMessages() = viewModelScope.launch(Dispatchers.IO) {
        lastMessages.value = database.messageDao().getLastMessages().stream()
            .collect(Collectors.toMap(Message::contact) { it })
    }
}