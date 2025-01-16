package com.crbt.data.core.data.util

import android.content.Context


fun getAppVersion(
    context: Context
): String {
    return context.packageManager.getPackageInfo(context.packageName, 0).versionName ?: "Unknown"
}


fun copyRightInfo(
    devTeam: String,
    fromYear: Int = 2024
): String {
    val currentYear =  java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
    return "${fromYear.yearToString(currentYear)} $devTeam"
}

fun Int.yearToString(year: Int): String {
    return when (this ==year) {
        true -> "$year"
        else -> "$this - $year"
    }
}