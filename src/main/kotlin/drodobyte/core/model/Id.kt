package drodobyte.core.model

import java.util.UUID

typealias Id = String // TODO as Value class

//typealias Id = UUID
//typealias Id = Long

private val implementor = Implementor.Str

/**
 * New random [Id]
 */
val newId: Id get() = implementor.factory.id()
val String?.asId: Id? get() = implementor.factory.id(this)
val Number?.asId: Id? get() = implementor.factory.id(this)


private enum class Implementor {
    Uuid, Str, Long;

    val factory
        get() = when (this) {
            Uuid -> UuidFactory
            Str -> StringFactory
            Long -> TODO()
        }
}

private interface Factory {
    fun id(): Id
    fun id(id: String?): Id?
    fun id(id: Number?): Id?
}

private val UuidFactory = object : Factory {
    override fun id() = UUID.randomUUID().toString()
    override fun id(id: String?) = id?.let { UUID.fromString(id).toString() }
    override fun id(id: Number?) = id(id?.toString())
}

private val StringFactory = object : Factory {
    override fun id() = UuidFactory.id()
    override fun id(id: String?) = id
    override fun id(id: Number?) = id(id?.toString())
}
