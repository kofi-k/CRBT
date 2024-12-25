package com.crbt.data.core.data.repository

import android.content.Context
import com.crbt.data.core.data.repository.network.CrbtNetworkRepository
import com.crbt.data.core.data.util.extractUserContacts
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class UserContactsRepo @Inject constructor(
    @ApplicationContext private val context: Context,
    private val crbtNetworkRepository: CrbtNetworkRepository,
) {

    suspend fun uploadUserContacts() {
        val contacts = extractUserContacts(context).map { it.second }
        crbtNetworkRepository.uploadUserContacts(contacts)
    }
}