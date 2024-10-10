package drodobyte.core.util

data class Link(
    val server: Server,
    val part: String
) {
    override fun toString() = "$server/$part"
}
