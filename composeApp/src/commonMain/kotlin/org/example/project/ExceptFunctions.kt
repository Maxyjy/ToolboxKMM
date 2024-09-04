package org.example.project

/**
 *
 *
 * @author YangJianyu
 * @date 2024/8/28
 */
expect fun unzipFile(filePath: String)

expect fun merge(logFiles: ArrayList<String>, mergeFilePath: String)

expect fun executeADB(adbCommand: Array<String>, callback: (String) -> Unit)

expect fun readFromFile(filePath: String, callback: (String) -> Unit)

expect fun filterOnlyRedTeaProcessId(
    processId: ArrayList<Int>,
    originalFilePath: String,
    filteredFilePath: String,
    getProcessIdFunction: (String) -> Int
)