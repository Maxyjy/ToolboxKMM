package org.example.project.adb

/**
 *
 *
 * @author YangJianyu
 * @date 2024/9/24
 */
object AdbFileExplorer {

    const val ROOT_PATH = ""

    fun getFileList(path: String) {
        AdbExecutor.exec(ADB_PATH_LIST_FILE, object : AdbExecuteCallback {
            override fun onPrint(line: String) {
                println(line)
            }
        })
    }

}