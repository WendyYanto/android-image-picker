package com.wendy.imagepickerdemo.main

import android.app.Application
import dev.wendyyanto.imagepicker.di.Injector
import dev.wendyyanto.imagepicker.dao.MediaDao

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Injector.store(MediaDao::class.java, MediaDao(this))
    }
}