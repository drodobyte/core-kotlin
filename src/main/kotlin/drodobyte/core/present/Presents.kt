package drodobyte.core.present

import io.reactivex.ObservableSource

abstract class Presents : Present() {

    protected abstract val presents: List<Present>
    override lateinit var source: ObservableSource<*>

    override fun start() {
        presents.onEach { it.start() }
    }

    override fun dispose() {
        presents.onEach { it.dispose() }
    }
}
