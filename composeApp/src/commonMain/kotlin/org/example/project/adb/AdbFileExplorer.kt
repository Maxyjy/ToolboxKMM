package org.example.project.adb

/**
 *
 *
 * @author YangJianyu
 * @date 2024/9/24
 */
object AdbFileExplorer {

    const val ROOT_PATH = ""

    fun getFileList(path: String, callback: (ArrayList<String>) -> Unit) {
        val cmd = ADB_PATH_LIST_FILE.replace(ADB_PATH_HOLDER, path)
        val fileList = ArrayList<String>()
        AdbExecutor.exec(cmd, object : AdbExecuteCallback {
            override fun onPrint(line: String) {
                fileList.add(line)
            }

            override fun onExit(exitCode: Int) {
                super.onExit(exitCode)
                if (exitCode == 0) {
                    callback.invoke(fileList)
                }
            }
        })
    }

    fun checkIsDirectory(path: String, callback: (Boolean) -> Unit) {
        val rawPath = path.replace(" ","\u0020")
        val cmd = ADB_PATH_CHECK_IS_DIRECTORY.replace(ADB_PATH_HOLDER, rawPath)
        AdbExecutor.exec(cmd, object : AdbExecuteCallback {
            override fun onPrint(line: String) {
                println("check is dir: $line")
                if (line == "true") {
                    callback.invoke(true)
                }
                if (line == "false") {
                    callback.invoke(false)
                }
            }

            override fun onExit(exitCode: Int) {
                super.onExit(exitCode)
            }
        })
    }

}