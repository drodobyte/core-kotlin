package drodobyte.core.util

/**
 * Cartesian Product for collections
 */
operator fun <T, U> Collection<T>.times(c2: Collection<U>) =
    flatMap { i1 -> c2.map { i2 -> i1 to i2 } }
