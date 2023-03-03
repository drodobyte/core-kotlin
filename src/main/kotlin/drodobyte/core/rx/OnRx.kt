package drodobyte.core.rx

interface OnRx : Dispose {
    fun <T> In<T>.io(item: (T) -> Unit) = Unit
    fun <T> In<T>.compute(item: (T) -> Unit) = Unit
}
