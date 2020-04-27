package com.wendy.imagepickerdemo.main

import android.app.Application
import com.wendy.imagepickerdemo.main.di.Injector
import com.wendy.imagepickerdemo.main.dao.MediaDao

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Injector.store(MediaDao::class.java, MediaDao(this))
    }
}