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
                fileList.add(path + line)
                println("child file:[$line]")
            }

            override fun onExit(exitCode: Int) {
                super.onExit(exitCode)
                if (exitCode == 0) {
                    callback.invoke(fileList)
                }
            }
        })
    }

}