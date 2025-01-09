package com.itengs.crbt

import android.app.Application
import com.itengs.crbt.util.ProfileVerifierLogger
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