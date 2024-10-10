package drodobyte.core.model

import java.util.UUID

typealias Id = UUID

/**
 * New random Id
 */
val newId get() = UUID.randomUUID()
val String.asId get() = UUID.fromString(this)
val Number.asId get() = toString().asId
