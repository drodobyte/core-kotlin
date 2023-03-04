package drodobyte.core.rx

import io.reactivex.Scheduler

interface OnRx : Dispose {
    fun <T> In<T>.io(item: (T) -> Unit) = Unit
    fun <T> In<T>.compute(item: (T) -> Unit) = Unit
    fun <T> In<T>.on(sched: Scheduler, item: (T) -> Unit) = Unit
}
