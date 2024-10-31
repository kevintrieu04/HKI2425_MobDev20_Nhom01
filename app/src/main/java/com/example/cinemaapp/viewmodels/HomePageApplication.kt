package com.example.cinemaapp.viewmodels

import android.app.Application
import com.example.cinemaapp.models.AdContainer
import com.example.cinemaapp.models.Container
import com.example.cinemaapp.models.DefaultAdContainer
import com.example.cinemaapp.models.DefaultAppContainer

class HomePageApplication : Application() {
    /** AppContainer instance used by the rest of classes to obtain dependencies */
    lateinit var container: Container
    lateinit var ad_container: AdContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
        ad_container = DefaultAdContainer()
    }
}