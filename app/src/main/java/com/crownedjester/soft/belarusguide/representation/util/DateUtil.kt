package com.crownedjester.soft.belarusguide.representation.util

import android.text.format.DateFormat
import java.util.*

object DateUtil {

    private const val datePattern = "dd.MM.yyyy"

    fun convertTimestampToDate(timestamp: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp * 1000L
        return DateFormat.format(datePattern, calendar).toString()
    }

}