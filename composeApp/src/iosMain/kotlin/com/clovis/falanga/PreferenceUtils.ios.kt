package com.clovis.falanga

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

actual fun getPreferences(pref: Any): PreferenceS? {
    return PreferenceSUtil(pref as DataStore<Preferences>)
}