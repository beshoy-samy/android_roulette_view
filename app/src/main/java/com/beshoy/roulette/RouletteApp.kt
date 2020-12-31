package com.beshoy.roulette

import androidx.multidex.MultiDexApplication
import timber.log.Timber

class RouletteApp : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}