package drodobyte.core.present

import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.util.logging.Level.SEVERE
import java.util.logging.Logger.getGlobal as logger

abstract class Present {

    protected abstract val source: ObservableSource<*>
    private lateinit var disposable: Disposable

    open fun start() {
        source.subscribe(object : Observer<Any> {
            override fun onSubscribe(d: Disposable) {
                disposable = d
            }

            override fun onError(e: Throwable) =
                logger().log(SEVERE, "Subscription error: ${this@Present.javaClass.simpleName}", e)

            override fun onComplete() = Unit
            override fun onNext(t: Any) = Unit
        })
    }

    open fun dispose() = disposable.dispose()
}
