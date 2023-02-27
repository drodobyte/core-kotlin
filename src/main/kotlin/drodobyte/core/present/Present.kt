package drodobyte.core.present

import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

abstract class Present {

    protected abstract val source: ObservableSource<*>
    private lateinit var disposable: Disposable

    open fun start() {
        source.subscribe(object : Observer<Any> {
            override fun onSubscribe(d: Disposable) {
                disposable = d
            }

            override fun onError(e: Throwable) {
                println("Error subscription ${this@Present.javaClass.simpleName}: $e")
            }

            override fun onComplete() {
            }

            override fun onNext(t: Any) {
            }
        })
    }

    open fun dispose() = disposable.dispose()
}
