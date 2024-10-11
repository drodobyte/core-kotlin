package drodobyte.core.case

import io.reactivex.ObservableSource

/**
 * Base Use-Case class: set logic to [src] (emissions will be ignored)
 */
abstract class Case {

    abstract val src: ObservableSource<*>
}
