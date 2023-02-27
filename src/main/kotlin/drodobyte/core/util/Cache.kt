package drodobyte.core.util

interface Cache<T> {

    fun put(item: T)

    fun get(): T

    fun clear()

    fun isCleared(): Boolean

    fun update(updater: (T) -> T) {
        if (!isCleared())
            put(updater(get()))
    }
}
