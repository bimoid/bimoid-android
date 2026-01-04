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
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_REMOTE_MESSAGING
import android.os.IBinder
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.CATEGORY_SERVICE
import androidx.core.app.NotificationCompat.PRIORITY_HIGH
import androidx.core.app.ServiceCompat
import dagger.hilt.android.AndroidEntryPoint
import io.github.bimoid.BimoidDatabase
import io.github.bimoid.R
import io.github.bimoid.cl.ContactListManager
import io.github.bimoid.connection.ConnectionManager
import io.github.bimoid.im.InstantMessagingManager
import io.github.bimoid.notification.NotificationManager.Companion.CHANNEL_ID
import io.github.bimoid.pres.PresenceManager
import io.github.bimoid.ui.theme.primary
import io.github.obimp.common.ByeReason
import io.github.obimp.common.DisconnectReason
import io.github.obimp.common.HelloError
import io.github.obimp.common.LoginError
import io.github.obimp.common.RegistrationResult
import io.github.obimp.connection.PlainObimpConnection
import io.github.obimp.listener.CommonListener
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


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        runBlocking(Dispatchers.IO) {
            database.accountDao().getAll().forEach {
                val obimpConnection = PlainObimpConnection()
                obimpConnection.addListener(object : CommonListener {
                    override fun onConnect() {
                        obimpConnection.login(it.username, it.password)
                    }

                    override fun onConnectError() {
                        println("Connect error")
                    }

                    override fun onDisconnect(disconnectReason: DisconnectReason) {
                        println("Disconnect by reason: $disconnectReason")
                    }

                    override fun onDisconnectByServer(byeReason: ByeReason) {
                        println("Disconnect by server, bye reason: $byeReason")
                    }

                    override fun onHelloError(helloError: HelloError) {
                        println("Hello error: $helloError")
                    }

                    override fun onLogin() {
                        println("Login")
                    }

                    override fun onLoginError(loginError: LoginError) {
                        println("Login error: $loginError")
                    }

                    override fun onRegistrationResult(registrationResult: RegistrationResult) {
                        println("Registration result: $registrationResult")
                    }
                })
                obimpConnection.addListener(ContactListManager)
                obimpConnection.addListener(PresenceManager)
                obimpConnection.addListener(instantMessagingManager)
                obimpConnection.connect(it.server, it.port)
                ConnectionManager.connections[obimpConnection] = Pair(it.username, it.password)
            }
        }

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setCategory(CATEGORY_SERVICE)
            .setPriority(PRIORITY_HIGH)
            .setOngoing(true)
            .setSilent(true)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Подключено")
            .setContentText("Аккаунтов: ${ConnectionManager.connections.size}")
            .setColor(primary.toArgb())
            .build()

        ServiceCompat.startForeground(this, 7023, notification, FOREGROUND_SERVICE_TYPE_REMOTE_MESSAGING)

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder? = null
}