package org.example.project

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.example.project.adb.AdbExecuteCallback
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.FileReader
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.zip.GZIPInputStream

/**
 *
 *
 * @author YangJianyu
 * @date 2024/8/28
 */
actual fun getSystemCurrentTimeMillis(): Long {
    return System.currentTimeMillis()
}

actual fun formatTime(timeMillis: Long): String {
    val sDateFormat = SimpleDateFormat("mm:ss")
    val time = sDateFormat.format(Date(timeMillis))
    return time
}

actual fun unzipFile(filePath: String) {
    val unzipPath = filePath.replace(".gz", "")
    val fis = FileInputStream(filePath)
    val gis = GZIPInputStream(fis)
    val fos = FileOutputStream(unzipPath)

    val buffer = ByteArray(1024)
    var len: Int
    while ((gis.read(buffer).also { len = it }) > 0) {
        fos.write(buffer, 0, len)
    }
    fos.close()
    gis.close()
    fis.close()
}

actual fun executeADB(adbCommand: String, callback: AdbExecuteCallback) {
    CoroutineScope(Dispatchers.Default).launch {
        try {
            // 设置 ADB 命令
            // 启动进程
            println(adbCommand)
            val process: Process = ProcessBuilder(adbCommand.split(" ")).start()

            // 读取命令输出
            val reader = process.inputReader()
            var line: String?
            while ((reader.readLine().also { line = it }) != null) {
                line?.let { callback.onPrint(it) }
                println(line)
            }
            val errorReader = process.errorReader()
            // 错误输出
            var errorLine: String?
            while ((errorReader.readLine().also { errorLine = it }) != null) {
                errorLine?.let { callback.onPrint(it) }
                println(errorLine)
            }
            // 等待命令执行完毕
            val exitCode: Int = process.waitFor()
            callback.onExit(exitCode)
            println("ADB command executed with exit code: $exitCode")
        } catch (e: Exception) {
            println(e.message)
        }
    }
}

actual fun merge(logFiles: ArrayList<String>, mergeFilePath: String) {
    val fos = FileOutputStream(File(mergeFilePath))
    for (logFile in logFiles) {
        val fis = FileInputStream(File(logFile))
        val buffer = ByteArray(2048)
        var length: Int
        while ((fis.read(buffer).also { length = it }) > 0) {
            fos.write(buffer, 0, length)
        }
        fis.close()
    }
    for (logFile in logFiles) {
        File(logFile).delete()
    }
    fos.close()
    println("Files concatenated successfully.")
}

actual fun readFromFile(filePath: String, callback: (String) -> Unit) {
    BufferedReader(FileReader(filePath)).use { br ->
        var line: String
        while ((br.readLine().also { line = it }) != null) {
            callback.invoke(line)
        }
    }
}

actual fun filterOnlyRedTeaProcessId(
    processId: ArrayList<Int>,
    originalFilePath: String,
    filteredFilePath: String,
    getProcessIdFunction: (String) -> Int
) {
    val reader =
        BufferedReader(FileReader(originalFilePath))
    val writer =
        BufferedWriter(FileWriter(filteredFilePath))
    var line: String
    while ((reader.readLine().also { line = it }) != null) {
        if (!line.contains("beginning of")) {
            val id: Int = getProcessIdFunction.invoke(line)
            for (i in processId.indices) {
                if (processId.contains(id)) {
                    if (line.contains("performTraversals: cancel draw reason")
                        || line.contains("DisplayManager")
                        || line.contains("W System")
                        || line.contains("VRI[")
                        || line.contains("InputTransport")
                        || line.contains("InputMethodManager")
                        || line.contains("SurfaceControl")
                        || line.contains("InputEventReceiver")
                        || line.contains("ClientTransactionHandler")
                        || line.contains("BufferQueueProducer")
                        || line.contains("InsetsSourceConsumer")
                        || line.contains("ImeFocusController")
                        || line.contains("IPCThreadState")
                        || line.contains("ResourcesCompat")
                        || line.contains("AconfigFlags")
                    ) {
                        break
                    }
                    writer.write(line)
                    writer.newLine()
                    break
                }
            }
        }
    }
    reader.close()
    writer.close()
}