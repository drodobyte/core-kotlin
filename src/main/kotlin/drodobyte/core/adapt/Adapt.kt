package drodobyte.core.adapt

import drodobyte.core.case.Case
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.util.logging.Level.SEVERE
import java.util.logging.Logger.getGlobal as logger

/**
 * Helper Adapter for Domain ([drodobyte.core.case.Case]) <-> Details layers
 */
abstract class Adapt {

    protected abstract val case: Case
    private lateinit var disposable: Disposable

    open fun start() {
        case.src.subscribe(object : Observer<Any> {
            override fun onSubscribe(d: Disposable) {
                disposable = d
            }

            override fun onError(e: Throwable) =
                logger().log(SEVERE, "Subscription error: ${this@Adapt.javaClass.simpleName}", e)

            override fun onComplete() = Unit
            override fun onNext(t: Any) = Unit
        })
    }

    open fun dispose() = disposable.dispose()
}
