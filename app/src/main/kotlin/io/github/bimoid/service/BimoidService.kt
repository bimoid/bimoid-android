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

package io.github.bimoid.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import dagger.hilt.android.AndroidEntryPoint
import io.github.bimoid.BimoidDatabase
import io.github.bimoid.cl.ContactListManager
import io.github.bimoid.connection.ConnectionManager
import io.github.bimoid.im.InstantMessagingManager
import io.github.bimoid.pres.PresenceManager
import io.github.obimp.connection.PlainObimpConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

/**
 * @author Alexander Krysin
 */
@AndroidEntryPoint
class BimoidService : Service() {
    @Inject
    lateinit var database: BimoidDatabase
    @Inject
    lateinit var instantMessagingManager: InstantMessagingManager

    override fun onCreate() {
        super.onCreate()
        runBlocking(Dispatchers.IO) {
            database.accountDao().getAll().forEach {
                val obimpConnection = PlainObimpConnection()
                obimpConnection.addListener(ContactListManager)
                obimpConnection.addListener(PresenceManager)
                obimpConnection.addListener(instantMessagingManager)
                obimpConnection.connect("bimoid.net", 7023)
                ConnectionManager.connections[obimpConnection] = Pair(it.username, it.password)
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        runBlocking(Dispatchers.IO) {
            ConnectionManager.connections.forEach { (connection, credentials) ->
                connection.login(credentials.first, credentials.second)
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder? = null
}