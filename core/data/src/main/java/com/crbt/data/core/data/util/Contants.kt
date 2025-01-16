package com.crbt.data.core.data.util

const val CHECK_BALANCE_USSD = "*804#"
const val RECHARGE_USSD = "*805*" // append voucher code and a #
const val CALL_ME_BACK_USSD = "*807*" // append phone number and a #
const val TRANSFER_USSD = "*807*" // append amount, *  phone number and a #
const val CRBT_REGISTER_USSD = "*822*1*" // append package id and a #
const val INTERNET_SPEED_CHECK_URL = "https://www.speedtest.net/"
const val CRBT_ADMIN_PORTAL = "https://www.crbtmusicpro.com "

const val STOP_TIMEOUT = 90_000L