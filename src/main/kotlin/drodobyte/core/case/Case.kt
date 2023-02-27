package drodobyte.core.case

import drodobyte.core.rx.In

/**
 * Base Use-Case class
 */
abstract class Case {

    abstract val result: In<*>
}
