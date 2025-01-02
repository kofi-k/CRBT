package com.crbt.data.core.data.repository

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import com.crbt.common.core.common.network.CrbtDispatchers
import com.crbt.common.core.common.network.Dispatcher
import com.crbt.data.core.data.repository.network.CrbtNetworkRepository
import com.crbt.data.core.data.util.extractUserContacts
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserMetaInfoCollectionRepo @Inject constructor(
    @ApplicationContext private val context: Context,
    private val crbtNetworkRepository: CrbtNetworkRepository,
    private val crbtPreferencesRepository: CrbtPreferencesRepository,
    @Dispatcher(CrbtDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) {

    suspend fun uploadUserContacts() {
        val contacts = extractUserContacts(context).map { it.second }
        if (!isContactsUpToDate(contacts)) {
            crbtPreferencesRepository.saveUserContacts(contacts)
            crbtNetworkRepository.uploadUserContacts(contacts)
        }
    }

    suspend fun getLastKnownLocation(): String = getLastKnownLocation(context) { location ->
        if (location != null) {
            "${location.latitude},${location.longitude}"
        } else {
            ""
        }
    }

    private suspend fun isContactsUpToDate(contacts: List<String>): Boolean {
        val currentContacts = crbtPreferencesRepository.getUserContacts()
        return contacts.joinToString(",") == currentContacts
    }

    private suspend fun getLastKnownLocation(
        context: Context,
        onLocationReceived: (Location?) -> String
    ): String = withContext(ioDispatcher) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        checkAndRequestPermission(context)
        val task = fusedLocationClient.lastLocation
        val location = task.await()
        onLocationReceived(location)
    }

    private fun checkAndRequestPermission(context: Context) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                (context as Activity),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }


    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }

}