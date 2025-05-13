package com.avsoftware.quilterdemo

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class QuilterApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        // hide this in Production
        Timber.plant(Timber.DebugTree())
    }
}