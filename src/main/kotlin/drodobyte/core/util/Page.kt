package drodobyte.core.util

import kotlin.math.max

data class Page(val page: Long = 0L, val size: Int = 20) {
    val first get() = copy(page = 0L)
    val next get() = copy(page = page + 1)
    val prev get() = copy(page = max(page - 1, 0))
    val isFirst = page == 0L
}
