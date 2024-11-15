package drodobyte.core.model

import drodobyte.core.rx.In
import drodobyte.core.rx.InOut
import drodobyte.core.rx.Rx
import drodobyte.core.rx.io
import drodobyte.core.rx.plus
import drodobyte.core.rx.switchMapAction
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

/**
 * Base Dao providing basic common access methods
 */
open class Dao<T : Model>(
    private val fetchAll: () -> In<Models<T>>,
    private val saveOne: (T) -> In<T>,
    private val updateOne: (T) -> In<T>,
    private val sched: Scheduler = Schedulers.io()
) : Rx() {

    val all: In<Models<T>> get() = updates + fetchAll().subscribeOn(sched)

    fun by(id: Id): In<T> = by { it.id == id }.map { it.first() }

    fun by(cond: (T) -> Boolean): In<Models<T>> = all.map { models -> models.filter { cond(it) } }

    val saveAll: InOut<Models<T>> = io<Models<T>>()

    val save: InOut<T> = io<T>()

    init {
        subs(
            (save.map(::listOf) + saveAll)
                .observeOn(sched)
                .switchMap(::saveOrUpdate)
                .switchMap { fetchAll() }
                .switchMapAction { updates + it }
        )
    }

    private val updates = io<Models<T>>()

    private fun saveOrUpdate(pets: Models<T>): In<Models<T>> =
        Observable.fromIterable(pets)
            .switchMap { if (it.isNone) saveOne(it) else updateOne(it) }
            .collectInto<Models<T>>(mutableListOf()) { result, model -> result as MutableList += model }
            .toObservable()
}
