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

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.github.bimoid.R

/**
 * @author Alexander Krysin
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TopBar(
    onMenuButtonClick: () -> Unit,
    onActionsButtonClick: () -> Unit,
    actionsMenuExpanded: Boolean,
    onActionsMenuDismissRequest: () -> Unit,
    onDisconnectClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    TopAppBar(
        modifier = Modifier.statusBarsPadding(),
        navigationIcon = {
            IconButton(onClick = onMenuButtonClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu"
                )
            }
        },
        title = {
            Text(text = stringResource(id = R.string.app_name))
        },
        actions = {
            IconButton(onClick = onActionsButtonClick) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Действия"
                )
                DropdownMenu(
                    expanded = actionsMenuExpanded,
                    onDismissRequest = onActionsMenuDismissRequest
                ) {
                    DropdownMenuItem(
                        onClick = onDisconnectClick,
                        text = { Text(text = "Отключиться") }
                    )
                    DropdownMenuItem(
                        onClick = onLogoutClick,
                        text = { Text(text = "Выйти из аккаунта") }
                    )
                }
            }
        }
    )
}