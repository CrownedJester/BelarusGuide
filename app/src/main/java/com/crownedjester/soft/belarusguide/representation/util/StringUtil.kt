package com.crownedjester.soft.belarusguide.representation.util

object StringUtil {
    private val BAD_STRINGS_ARRAY =
        listOf(
            "</p>",
            "&laquo;",
            "&mdash;",
            "&raquo;",
            "<p>",
            "&ldquo;",
            "&rdquo;",
            "&nbsp;",
            "&ndash;",
            "&rsquo;",
            "<p style=\"margin-left:0cm; margin-right:0cm\">"
        )

    fun String.formatPlaceDescription(): String {
        var result = this
        BAD_STRINGS_ARRAY.onEach { badString ->
            if (result.contains(badString))
                result = result.replace(badString, "")
        }
        return result
    }
}