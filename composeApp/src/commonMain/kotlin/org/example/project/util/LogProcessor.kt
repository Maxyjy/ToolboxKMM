package org.example.project.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.io.IOException
import kotlinx.io.files.FileMetadata
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import org.example.project.filterOnlyRedTeaProcessId
import org.example.project.merge
import org.example.project.readFromFile
import org.example.project.unzipFile

/**
 *
 *
 * @author YangJianyu
 * @date 2024/8/28
 */
class LogProcessor {

    companion object {

        private const val APP_LOG_PREFIX = "applogcat-log."
        private const val FILE_NAME_MERGED_FULL_LOG = "merged_full.log"
        private const val FILE_NAME_MERGED_PACKAGE_ONLY_LOG = "merged_package_only.log"
        private const val MERGE_FILE = "/$FILE_NAME_MERGED_FULL_LOG"
        private const val PACKAGE_ONLY_FILE = "/$FILE_NAME_MERGED_PACKAGE_ONLY_LOG"

        interface LogProcessListener {
            fun onStep(progress: Float, info: String)
            fun onResult(isSuccess: Boolean)
        }

        fun process(rootPath: String, listener: LogProcessListener) {
            CoroutineScope(Dispatchers.Default).launch {
                val rootMeta = SystemFileSystem.metadataOrNull(Path(rootPath))
                try {
                    rootMeta?.let {
                        if (!rootMeta.isDirectory) {
                            listener.onStep(0f, "The path is not a directory")
                        }

                        // find all gz file
                        val zipFiles = findAllGzFile(rootMeta, rootPath)
                        listener.onStep(0.25f, "Log zip file found, total size [${zipFiles.size}]")

                        // decompress all gz file
                        val logFiles = decompressGzFiles(zipFiles)
                        listener.onStep(0.4f, "Decompress all log zip file [${zipFiles.size}]")

                        // merge all log file
                        merge(logFiles, "$rootPath$MERGE_FILE")
                        listener.onStep(0.65f, "Merge log result file [$rootPath$MERGE_FILE]")

                        // find all process id
                        val processId = findProcessId("$rootPath$MERGE_FILE")
                        if (processId.size > 0) {
                            listener.onStep(0.80f, "Found all process id [$processId]")
                        } else {
                            listener.onStep(0.80f, "Process id not found !")
                        }

                        if (processId.size > 0) {
                            // filter only by process
                            filterOnlyRedTeaProcessId(
                                processId, "$rootPath$MERGE_FILE", "$rootPath$PACKAGE_ONLY_FILE"
                            ) { line ->
                                return@filterOnlyRedTeaProcessId getProcessIdByLogLine(line)
                            }
                        }

                        listener.onStep(
                            1f,
                            "Log filter by package name result file [$rootPath$PACKAGE_ONLY_FILE]"
                        )
                        listener.onResult(true)
                    }
                } catch (e: Exception) {
                    listener.onStep(0f, "exception :[${e.message}]")
                    listener.onResult(false)
                }
            }
        }

        private fun findAllGzFile(rootMeta: FileMetadata, rootPath: String): ArrayList<Path> {
            val zipFiles = ArrayList<Path>()
            // find all gz file
            val childrenFiles = SystemFileSystem.list(Path(rootPath))
            if (rootMeta.isDirectory && childrenFiles.isNotEmpty()) {
                childrenFiles.forEach {
                    if (it.name.contains(APP_LOG_PREFIX) && it.name.contains(".gz")) {
                        zipFiles.add(it)
                    }
                }
            }
            // sort all gz file by index in file name
            zipFiles.sortWith { a, b -> getTimeOfFile(a.name).compareTo(getTimeOfFile(b.name)) }
            return zipFiles
        }

        private fun decompressGzFiles(zipFiles: ArrayList<Path>): ArrayList<String> {
            val logFiles = ArrayList<String>()
            zipFiles.forEach {
                println(it.name)
                unzipFile(it.toString())
                logFiles.add(it.toString().replace(".gz", ""))
            }
            return logFiles
        }

        //I979.20241130-114105.gz
        private fun getTimeOfFile(rawFileName: String): Long {
            var fileName = rawFileName
            fileName = fileName.replace(APP_LOG_PREFIX, "")
            fileName = fileName.replace(".gz", "")
            fileName = fileName.substring(5, fileName.length)
            fileName = fileName.replace("-", "")
            return fileName.toLong()
        }

        private fun findProcessId(fileName: String): ArrayList<Int> {
            val processId = ArrayList<Int>()
            try {
                readFromFile(fileName) { line ->
                    if (!line.contains("beginning of")) {
                        val tag: String = getTagByLogLine(line)
                        val id: Int = getProcessIdByLogLine(line)
                        if (tag.contains("RTRoaming") || tag.contains("RedTea")) {
                            if (!processId.contains(id)) {
                                println("process id find in: $line")
                                processId.add(id)
                            }
                        }
                    }
                }
                return processId
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return processId
        }

        private fun getTagByLogLine(rawLine: String): String {
            var line = rawLine
            line = line.replace("\\s+".toRegex(), " ")
            var tag = ""
            try {
                tag = line.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[5]
            } catch (e: Exception) {
                println("get log tag wrong line [$line]")
            }
            return tag
        }

        private fun getProcessIdByLogLine(rawLine: String): Int {
            var line = rawLine
            line = line.replace("\\s+".toRegex(), " ")
            var processId = 0
            try {
                val process =
                    line.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[2]
                processId = process.toInt()
            } catch (e: Exception) {
                println("get process id wrong line [$line]")
            }
            return processId
        }

    }


}