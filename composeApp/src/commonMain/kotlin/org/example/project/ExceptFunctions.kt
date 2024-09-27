package org.example.project

import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import org.example.project.adb.AdbExecuteCallback

/**
 *
 *
 * @author YangJianyu
 * @date 2024/8/28
 */
expect fun getSystemName(): String

expect fun getSystemCurrentTimeMillis(): Long

expect fun formatTime(timeMillis: Long): String

expect fun unzipFile(filePath: String)

expect fun merge(logFiles: ArrayList<String>, mergeFilePath: String)

expect fun executeADB(adbCommand: String, callback: AdbExecuteCallback)

expect fun readFromFile(filePath: String, callback: (String) -> Unit)

expect fun filterOnlyRedTeaProcessId(
    processId: ArrayList<Int>,
    originalFilePath: String,
    filteredFilePath: String,
    getProcessIdFunction: (String) -> Int
)

expect fun dataStorePreferences(
    corruptionHandler: ReplaceFileCorruptionHandler<Preferences>?,
    coroutineScope: CoroutineScope,
    migrations: List<DataMigration<Preferences>>,
): DataStore<Preferences>?