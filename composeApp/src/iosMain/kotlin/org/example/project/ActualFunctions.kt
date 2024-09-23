package org.example.project

import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import org.example.project.adb.AdbExecuteCallback
import org.example.project.util.SETTINGS_PREFERENCES
import org.example.project.util.createDataStoreWithDefaults

/**
 *
 *
 * @author YangJianyu
 * @date 2024/8/28
 */
actual fun getSystemCurrentTimeMillis(): Long {
    return 0L
}

actual fun formatTime(timeMillis: Long): String {
    return ""
}

actual fun unzipFile(filePath: String) {}

actual fun executeADB(adbCommand: String, callback: AdbExecuteCallback){

}

actual fun merge(logFiles: ArrayList<String>, mergeFilePath: String) {}

actual fun readFromFile(filePath: String, callback: (String) -> Unit) {}

actual fun filterOnlyRedTeaProcessId(
    processId: ArrayList<Int>,
    originalFilePath: String,
    filteredFilePath: String,
    getProcessIdFunction: (String) -> Int
) {
}

actual fun dataStorePreferences(
    corruptionHandler: ReplaceFileCorruptionHandler<Preferences>?,
    coroutineScope: CoroutineScope,
    migrations: List<DataMigration<Preferences>>
): DataStore<Preferences>? = createDataStoreWithDefaults(
    corruptionHandler = corruptionHandler,
    migrations = migrations,
    coroutineScope = coroutineScope,
    path = { SETTINGS_PREFERENCES }
)