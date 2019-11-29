package com.drodobyte.core.kotlin.entity

interface Entity {
    val id: Long?

    fun isSame(other: Entity) =
        !isNew() && id == other.id && javaClass == other.javaClass

    fun isNew(): Boolean =
        id == null
}

fun Iterable<Entity>.nextId() = map { it.id }.maxBy { it ?: 0 } ?: 0

fun <T : Entity> MutableList<T>.replace(entity: T) {
    this[indexOfFirst { it.isSame(entity) }] = entity
}
