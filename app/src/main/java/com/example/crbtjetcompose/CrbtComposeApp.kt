package com.example.crbtjetcompose

import android.app.Application
import com.example.crbtjetcompose.util.ProfileVerifierLogger
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class CrbtComposeApp : Application() {

    @Inject
    lateinit var profileVerifierLogger: ProfileVerifierLogger

    override fun onCreate() {
        super.onCreate()
        profileVerifierLogger()
    }
}