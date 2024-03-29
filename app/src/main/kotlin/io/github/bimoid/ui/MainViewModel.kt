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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.bimoid.BimoidDatabase
import io.github.bimoid.data.entity.Account
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Alexander Krysin
 */
@HiltViewModel
class MainViewModel @Inject constructor(private val database: BimoidDatabase) : ViewModel() {
    fun addAccount(server: String, username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            database.accountDao()
                .insert(Account(server = server, username = username, password = password))
        }
    }

    fun exitFromAccounts() {
        viewModelScope.launch(Dispatchers.IO) {
            database.accountDao().getAll().forEach(database.accountDao()::delete)
        }
    }
}