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

package io.github.bimoid.cl

import androidx.compose.runtime.mutableStateOf
import io.github.obimp.cl.*
import io.github.obimp.listener.ContactListListener

/**
 * @author Alexander Krysin
 */
object ContactListManager : ContactListListener {
    val contactList = mutableStateOf<List<ContactListItem>?>(null)

    override fun onAddContactListItem(contactListItemAddingResult: AddingResult) {}

    override fun onAuthorizationReply(
        accountName: String,
        authorizationReply: AuthorizationReply
    ) {}

    override fun onAuthorizationRequest(accountName: String, reason: String) {}

    override fun onAuthorizationRevoke(accountName: String, reason: String) {}

    override fun onBeginUpdate() {}

    override fun onContactListItemOperation(
        contactListItem: ContactListItem,
        operation: Operation
    ) {}

    override fun onContactListLoaded(items: List<ContactListItem>) {
        this.contactList.value = items
    }

    override fun onContactListParameters(contactListParameters: ContactListParameters) {}

    override fun onContactListVerify(contactListMD5Hash: ByteArray) {}

    override fun onDeleteContactListItem(contactListItemDeletionResult: DeletionResult) {}

    override fun onEndUpdate() {}

    override fun onOffauthDone() {}

    override fun onUpdateContactListItem(
        contactListItemUpdateOperationResult: UpdateOperationResult
    ) {}
}