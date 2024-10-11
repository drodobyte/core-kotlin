package drodobyte.core.adapt

import drodobyte.core.case.Case

/**
 * Compound [drodobyte.core.adapt.Adapt] helper, use [all] to collect the adapts
 */
abstract class Adapts : Adapt() {

    protected abstract val all: List<Adapt>
    override lateinit var case: Case // Not used

    override fun start() {
        all.onEach { it.start() }
    }

    override fun dispose() {
        all.onEach { it.dispose() }
    }
}
