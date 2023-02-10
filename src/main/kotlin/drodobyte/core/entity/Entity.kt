package drodobyte.core.entity

open class Entity(open val id: Long = NONE) {

    open infix fun isSame(other: Entity) =
        id == other.id && javaClass == other.javaClass

    open val isNone get() = id == NONE

    companion object {
        val None = Entity()
    }
}

val Iterable<Entity>.nextId get() = (maxBy { it.id }?.id ?: NONE) + 1

infix fun MutableList<Entity>.replace(entity: Entity) {
    this[indexOfFirst { it.isSame(entity) }] = entity
}

private const val NONE = 0L
