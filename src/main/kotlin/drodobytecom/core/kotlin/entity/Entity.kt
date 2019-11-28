package drodobytecom.core.kotlin.entity

interface Entity {
    companion object {
        const val NEW = 0L
    }

    val id: Long
    fun isSame(other: Entity) =
        !isNew() && !other.isNew() && id == other.id && javaClass == other.javaClass

    fun isNew(): Boolean =
        id == NEW
}


fun Iterable<Entity>.nextId() = map { it.id }.maxBy { it } ?: Entity.NEW + 1

fun <T : Entity> MutableList<T>.replace(entity: T) {
    this[indexOfFirst { it.isSame(entity) }] = entity
}
