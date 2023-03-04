package drodobyte.core.rx

import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers.computation
import io.reactivex.schedulers.Schedulers.io
import java.util.logging.Level.SEVERE
import java.util.logging.Logger

open class Rx(private val owner: Any? = null) : OnRx {

    private val disposable = CompositeDisposable()

    fun <T> on(sched: Scheduler, `in`: () -> In<T>) = subs(`in`, sched)
    fun <T> onIo(`in`: () -> In<T>) = subs(`in`, io())
    fun <T> onCompute(`in`: () -> In<T>) = subs(`in`, computation())

    override fun <T> In<T>.on(sched: Scheduler, item: (T) -> Unit) = subs(item, sched)
    override fun <T> In<T>.io(item: (T) -> Unit) = subs(item, io())
    override fun <T> In<T>.compute(item: (T) -> Unit) = subs(item, computation())

    override fun dispose() = disposable.dispose()

    protected fun <T> subs(items: () -> (In<T>), sched: Scheduler) =
        add(items().observeOn(sched).subscribeOn(sched).subscribe({}, ::log))

    protected fun <T> In<T>.subs(item: (T) -> Unit, sched: Scheduler) =
        add(observeOn(sched).subscribeOn(sched).subscribe(item, ::log))

    protected open fun log(it: Throwable) {
        Logger.getGlobal().log(SEVERE, "Rx subscription error, owner:${owner ?: this}", it)
    }

    private fun add(d: Disposable) {
        disposable.add(d)
    }
}
