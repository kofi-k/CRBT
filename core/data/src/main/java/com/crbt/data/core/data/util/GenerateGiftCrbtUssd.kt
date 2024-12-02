package com.crbt.data.core.data.util

fun generateGiftCrbtUssd(normalUssd: String, recipientPhoneNumber: String): String {
    if (recipientPhoneNumber.isBlank()) {
        throw IllegalArgumentException("Recipient phone number is required for gifting.")
    }

    // Split the normal USSD to identify the song ID
    // Example: normalUssd = *822*11*3*55026513*1*1*1#
    // gift ussd format *822*11*3*55026513*1*1*2*phoneNumber*1#
    val ussdParts = normalUssd.split("*")
    if (ussdParts.size < 7) {
        throw IllegalArgumentException("Invalid USSD format.")
    }

    return ussdParts.dropLast(1).joinToString("*") + "*2*$recipientPhoneNumber*1#"
}