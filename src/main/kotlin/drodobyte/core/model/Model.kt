package drodobyte.core.model

interface Model {
    val id: Long?

    fun isSame(other: Model) =
        !isNew() && id == other.id && javaClass == other.javaClass

    fun isNew(): Boolean =
        id == null
}

typealias Models<T> = List<T>

fun Iterable<Model>.nextId() = map { it.id }.maxBy { it ?: 0 } ?: 0

fun <T : Model> MutableList<T>.replace(entity: T) {
    this[indexOfFirst { it.isSame(entity) }] = entity
}
