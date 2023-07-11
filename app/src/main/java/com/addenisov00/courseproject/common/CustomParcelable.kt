package com.addenisov00.courseproject.common

import android.os.Build
import android.os.Bundle
import android.os.Parcelable

@Suppress("DEPRECATION")
inline fun <reified T : Parcelable> Bundle.customGetParcelable(key: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelable(key, T::class.java)
    } else {
        getParcelable(key) as? T
    }
}