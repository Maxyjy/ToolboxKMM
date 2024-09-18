package org.example.project.util

import kotlinx.io.bytestring.encode
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

/**
 *
 *
 * @author YangJianyu
 * @date 2024/9/14
 */
object Base64Util {

    @OptIn(ExperimentalEncodingApi::class)
    fun base64(string: String): String {
        val byteArray = string.encodeToByteArray()
        return Base64.encode(byteArray)
    }

}