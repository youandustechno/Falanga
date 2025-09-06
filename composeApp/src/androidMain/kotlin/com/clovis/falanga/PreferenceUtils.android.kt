package com.clovis.falanga

import android.content.Context

actual fun getPreferences(pref: Any): PreferenceS? {
    return PreferenceAndroid(pref as Context)
}