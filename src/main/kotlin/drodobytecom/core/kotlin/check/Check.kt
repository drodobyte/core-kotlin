package drodobytecom.core.kotlin.check

import io.reactivex.Observable

class Check {

    fun <Err> on(rules: Rules<Err>.() -> Unit): OnError<Err> {
        val builder = Rules<Err>(mutableListOf())
        builder.rules()
        return OnError(builder.errors)
    }

    class Rules<Err>(internal val errors: MutableList<Err>) {
        fun isBlank(str: String, err: Err) {
            isTrue(str.isBlank(), err)
        }

        fun isNotBlank(str: String, err: Err) {
            isTrue(str.isNotBlank(), err)
        }

        fun isTrue(condition: Boolean, err: Err) {
            check(condition, err)
        }

        private fun check(condition: Boolean, err: Err) {
            if (!condition && err !in errors)
                errors += err
        }
    }

    class OnError<Err>(private val items: List<Err>) {

        /** Observable version **/
        val onError: Observable<Err>
            get() = if (items.isEmpty())
                Observable.empty<Err>()
            else
                Observable.error<Err>(CheckException(items))

        /** Async version **/
        fun onError(action: (List<Err>) -> Unit) {
            if (items.isNotEmpty())
                action(items)
        }
    }

    class CheckException(val errors: List<*>) : Exception()
}