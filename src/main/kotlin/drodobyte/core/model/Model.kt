package drodobyte.core.model

import drodobyte.core.util.IdSerializer
import kotlinx.serialization.Serializable

interface Model {
    val id: @Serializable(IdSerializer::class) Id?

    fun isSame(other: Model) =
        !isNone && id == other.id && javaClass == other.javaClass

    val isNone get() = id == null
}

typealias Models<T> = List<T>

fun <T : Model> MutableList<T>.replace(entity: T) {
    this[indexOfFirst { it.isSame(entity) }] = entity
}
