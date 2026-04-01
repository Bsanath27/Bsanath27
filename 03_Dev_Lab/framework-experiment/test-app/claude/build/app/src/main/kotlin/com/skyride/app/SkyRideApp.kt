package com.skyrik.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for Skyrik.
 * Annotated with @HiltAndroidApp to trigger Hilt's code generation,
 * including a base class for the application that serves as the
 * application-level dependency container.
 */
@HiltAndroidApp
class SkyrikApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // Initialize any app-wide SDKs here (analytics, crash reporting, etc.)
    }
}
