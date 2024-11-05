package org.example.project.util

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 *
 *
 * @author YangJianyu
 * @date 2024/11/5
 */
object TimeUtil {

    fun getReadableTime(): String {
        val localDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        return "${localDateTime.year}-${localDateTime.monthNumber}-${localDateTime.dayOfMonth}_${localDateTime.hour}_${localDateTime.minute}_${localDateTime.second}"
    }

}