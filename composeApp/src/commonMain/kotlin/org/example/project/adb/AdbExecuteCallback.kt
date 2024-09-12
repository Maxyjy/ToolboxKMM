package org.example.project.adb

/**
 *
 *
 * @author YangJianyu
 * @date 2024/9/11
 */
interface AdbExecuteCallback {
    fun onPrint(line: String)
    fun onExit(exitCode: Int) {}
}