package drodobyte.core.adapt

import io.reactivex.ObservableSource

/**
 * Helper Compound [drodobyte.core.adapt.Adapt]
 */
abstract class Adapts : Adapt() {

    protected abstract val adapts: List<Adapt>
    override lateinit var source: ObservableSource<*>

    override fun start() {
        adapts.onEach { it.start() }
    }

    override fun dispose() {
        adapts.onEach { it.dispose() }
    }
}
