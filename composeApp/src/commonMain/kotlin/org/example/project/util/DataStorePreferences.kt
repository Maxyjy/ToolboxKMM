package org.example.project.util

import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import okio.Path.Companion.toPath
import org.example.project.dataStorePreferences

/**
 *
 *
 * @author YangJianyu
 * @date 2024/9/20
 */
interface CoreComponent {
    val appPreferences: AppPreferences
}

internal class CoreComponentImpl internal constructor() : CoreComponent {

    private val dataStore: DataStore<Preferences>? = dataStorePreferences(
        corruptionHandler = null,
        coroutineScope = CoroutineScope(Dispatchers.IO),
        migrations = emptyList()
    )

    override val appPreferences: AppPreferencesImpl = dataStore?.let { AppPreferencesImpl(it) }!!
}

internal const val SETTINGS_PREFERENCES = "settings_preferences.preferences_pb"

internal fun createDataStoreWithDefaults(
    corruptionHandler: ReplaceFileCorruptionHandler<Preferences>? = null,
    coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default),
    migrations: List<DataMigration<Preferences>> = emptyList(),
    path: () -> String,
) = PreferenceDataStoreFactory
    .createWithPath(
        corruptionHandler = corruptionHandler,
        scope = coroutineScope,
        migrations = migrations,
        produceFile = {
            path().toPath()
        }
    )

interface AppPreferences {
    suspend fun getString(key: String): String?
    suspend fun putString(key: String, value: String)
}

object AppPreferencesKey {
    const val TARGET_PACKAGE_NAME = "APP_PREFERENCES_TARGET_PACKAGE_NAME"
    const val HYPER_COMM_APK_PATH = "APP_PREFERENCES_HYPER_COMM_APK_PATH"
    const val RED_TEA_MOBILE_APK_PATH = "APP_PREFERENCES_RED_TEA_MOBILE_APK_PATH"
}

internal class AppPreferencesImpl(
    private val dataStore: DataStore<Preferences>
) : AppPreferences {

    override suspend fun getString(key: String): String? =
        dataStore.data.map { preferences ->
            preferences[stringPreferencesKey(key)]
        }.first()


    override suspend fun putString(key: String, value: String) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey(key)] = value
        }
    }
}