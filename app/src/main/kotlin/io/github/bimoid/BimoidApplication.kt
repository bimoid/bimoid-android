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

package io.github.bimoid

import android.app.Application
import android.os.Build
import dagger.hilt.android.HiltAndroidApp
import io.github.obimp.util.LibVersion
import io.github.obimp.util.SystemInfoUtil
import io.github.obimp.util.Version

/**
 * @author Alexander Krysin
 */
@HiltAndroidApp
class BimoidApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val version = BuildConfig.VERSION_NAME.split(".").map(String::toInt)
        LibVersion.name = "Bimoid"
        LibVersion.version = Version(version[0], version[1], version[2], 0)
        SystemInfoUtil.osName = "Android ${Build.VERSION.RELEASE}"
    }
}