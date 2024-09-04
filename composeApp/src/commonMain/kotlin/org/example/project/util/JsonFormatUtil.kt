package org.example.project.util

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

/**
 * Json Formatter
 *
 * @author YangJianyu
 * @date 2024/8/28
 */
class JsonFormatUtil {

    companion object {
        val format = Json { prettyPrint = true }

        fun format(json: String): String {
            val ob = format.decodeFromString<JsonElement>(json)
            return format.encodeToString(ob)
        }
    }

}