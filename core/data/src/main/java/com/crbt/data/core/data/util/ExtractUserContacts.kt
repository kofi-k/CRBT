package com.crbt.data.core.data.util

import android.content.Context
import android.provider.ContactsContract

fun extractUserContacts(context: Context): List<Pair<String, String>> {
    val contactsList = mutableListOf<Pair<String, String>>()

    val contentResolver = context.contentResolver
    val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
    val projection = arrayOf(
        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
        ContactsContract.CommonDataKinds.Phone.NUMBER
    )

    val cursor = contentResolver.query(uri, projection, null, null, null)

    cursor?.use {
        val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
        val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

        while (it.moveToNext()) {
            val name = it.getString(nameIndex)
            val number = it.getString(numberIndex)
            contactsList.add(Pair(name, number))
        }
    }

    return contactsList
}