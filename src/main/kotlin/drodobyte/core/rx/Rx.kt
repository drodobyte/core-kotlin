package drodobyte.core.rx

import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers.computation
import io.reactivex.schedulers.Schedulers.io
import java.util.logging.Level.SEVERE
import java.util.logging.Logger

open class Rx(private val owner: Any? = null) : OnRx {

    private val disposable = CompositeDisposable()

    fun <T> onIo(`in`: () -> (In<T>)) = subscribeToSched(`in`, {}, io())
    fun <T> onCompute(`in`: In<T>, item: (T) -> Unit) = `in`.compute(item)

    override fun <T> In<T>.io(item: (T) -> Unit) = subscribeToSched(item, io())
    override fun <T> In<T>.compute(item: (T) -> Unit) = subscribeToSched(item, computation())

    override fun dispose() = disposable.dispose()

    protected fun <T> In<T>.subscribeToSched(item: (T) -> Unit, scheduler: Scheduler) =
        subscribeToSched({ this }, item, scheduler)

    protected fun <T> subscribeToSched(items: () -> (In<T>), item: (T) -> Unit, sched: Scheduler) {
        disposable.add(
            items()
                .subscribeOn(sched)
                .observeOn(sched)
                .subscribe(item, ::log)
        )
    }

    protected open fun log(it: Throwable) {
        Logger.getGlobal().log(SEVERE, "Rx subscription error, owner:${owner ?: this}", it)
    }
}
