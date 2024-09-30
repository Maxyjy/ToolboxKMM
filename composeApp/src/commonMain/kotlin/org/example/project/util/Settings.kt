package org.example.project.util

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get


/**
 *
 *
 * @author YangJianyu
 * @date 2024/9/20
 */

object SettingsDelegate : AppPreferences {

    private val settings by lazy {
        Settings()
    }

    override suspend fun getString(key: String): String? {
        return settings[key]
    }

    override suspend fun putString(key: String, value: String) {
        settings.putString(key, value)
    }

}

internal const val SETTINGS_PREFERENCES = "settings_preferences.preferences_pb"

interface AppPreferences {
    suspend fun getString(key: String): String?
    suspend fun putString(key: String, value: String)
}

object AppPreferencesKey {
    const val ANDROID_HOME_PATH = "ANDROID_HOME_PATH"
    const val TARGET_PACKAGE_NAME = "APP_PREFERENCES_TARGET_PACKAGE_NAME"
    const val HYPER_COMM_APK_PATH = "APP_PREFERENCES_HYPER_COMM_APK_PATH"
    const val RED_TEA_MOBILE_APK_PATH = "APP_PREFERENCES_RED_TEA_MOBILE_APK_PATH"
}
