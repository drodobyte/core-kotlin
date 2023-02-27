package drodobyte.core.util

import java.text.DateFormat
import java.util.*

val Date.formatted: String get() = DateFormat.getDateInstance().format(this)

val String.date: Date get() = DateFormat.getDateInstance().parse(this)

val CharSequence.date get() = toString().date

val CharSequence.dateOrNull: Date? get() = toString().dateOrNull

val String.dateOrNull: Date? get() = kotlin.runCatching { date }.getOrNull()

val Triple<Int, Int, Int>.date: Date
    get() = Calendar.getInstance().apply {
        set(Calendar.YEAR, first)
        set(Calendar.MONTH, second)
        set(Calendar.DAY_OF_MONTH, third)
    }.time

val Date.calendar: Calendar get() = Calendar.getInstance().also { it.time = this }
