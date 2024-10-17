package drodobyte.core.util

val Any.objStr
    get() = let { obj ->
        obj.javaClass.let { clazz ->
            "${clazz.simpleName} = { " +
                clazz.declaredFields
                    .map { it.isAccessible = true; it.name to it.get(obj) }
                    .objStr + " }"
        }
    }

val Collection<Pair<Any, Any?>>.objStr
    get() = filter { it.second != null }
        .joinToString(", ") { "${it.first}=${it.second}" }
