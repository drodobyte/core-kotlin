package drodobyte.core.util

import kotlin.properties.ReadOnlyProperty

interface Bean<T> : ReadOnlyProperty<Any, T>
