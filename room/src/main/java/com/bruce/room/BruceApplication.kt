package com.bruce.room

import android.app.Application

/**
 *
 *@author xujian
 *@date 2021/9/25
 */
class BruceApplication : Application() {
    companion object {
        @JvmStatic
        lateinit var instance: BruceApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}