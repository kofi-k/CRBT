package com.crbt.data.core.data.util

import android.net.TrafficStats
import kotlinx.coroutines.delay


suspend fun measureInternetSpeedWithTrafficStats(): Double {
    val initialRxBytes = TrafficStats.getTotalRxBytes()
    val initialTxBytes = TrafficStats.getTotalTxBytes()

    delay(1000) // Measure over a period of 1 second

    val finalRxBytes = TrafficStats.getTotalRxBytes()
    val finalTxBytes = TrafficStats.getTotalTxBytes()

    val rxBytes = finalRxBytes - initialRxBytes
    val txBytes = finalTxBytes - initialTxBytes

    val totalBytes = rxBytes + txBytes

    return (totalBytes / 1024.0) // Speed in KB/s
}