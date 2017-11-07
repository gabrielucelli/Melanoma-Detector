package br.com.gabrieucelli.melanomadetector

import android.app.Application
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric



/**
 * Created by Gabriel on 07/11/2017.
 */

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Fabric.with(this, Crashlytics())
    }
}