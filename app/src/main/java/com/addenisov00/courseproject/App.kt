package com.addenisov00.courseproject

import android.app.Application
import android.content.Context
import com.addenisov00.courseproject.common.di.AppComponent
import com.addenisov00.courseproject.common.di.DaggerAppComponent


class App : Application() {
    lateinit var appComponent: AppComponent
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)
    }

}

fun Context.getAppComponent(): AppComponent = (this.applicationContext as App).appComponent