package com.example.cinemaapp.viewmodels

import android.app.Application
import com.example.cinemaapp.models.AdContainer
import com.example.cinemaapp.models.DefaultAdContainer

class HomePageApplication : Application() {
    /** AppContainer instance used by the rest of classes to obtain dependencies */
    lateinit var ad_container: AdContainer
    override fun onCreate() {
        super.onCreate()
        ad_container = DefaultAdContainer()
    }
}