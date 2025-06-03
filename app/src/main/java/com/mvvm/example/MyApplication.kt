package com.mvvm.example


import android.content.Context
import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp
import java.util.Locale

@HiltAndroidApp
class MyApplication : MultiDexApplication() {

    companion object {
        var appContext: Context? = null
            private set

        // Getter to access Singleton instance
        //     Singleton instance;
        var instance: MyApplication? = null
            private set
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext

        instance = this

    }


    private fun setApplicationLocale(locale: String = "en") {

        val dm = resources?.displayMetrics
        val config = resources.configuration
        config.setLocale(Locale(locale.lowercase(Locale.getDefault())))
        resources.updateConfiguration(config, dm)
        //resources.updateConfiguration(config, dm)
    }
}