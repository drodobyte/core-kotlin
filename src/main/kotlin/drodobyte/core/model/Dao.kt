package drodobyte.core.model

import drodobyte.core.rx.In
import drodobyte.core.rx.InOut
import drodobyte.core.rx.Rx
import drodobyte.core.rx.hot
import drodobyte.core.rx.plus
import drodobyte.core.rx.switchMapAction
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

typealias Models<T> = List<T>

open class Dao<T : Model>(
    private val fetchAll: () -> In<Models<T>>,
    private val saveOne: (T) -> In<T>,
    private val sched: Scheduler = Schedulers.io()
) : Rx() {
    private val fetch = hot<Models<T>>()

    val all: In<Models<T>> get() = fetch.startWith(fetchAll())!!.subscribeOn(sched)

    fun by(id: Long): In<T> = all.map { it.first { model -> model.id == id } }!!

    fun by(cond: (T) -> Boolean): In<Models<T>> = all.map { models -> models.filter { cond(it) } }!!

    val saveAll: InOut<Models<T>> = hot()

    val save: InOut<T> = hot()

    init {
        on(sched) { saveAll.switchMap(::save).switchMapAction { fetch + it } }
        on(sched) { save.map { listOf(it) }.switchMap(::save).switchMapAction { fetch + it } }
        on(sched) { fetch.switchMap { fetchAll() } }
    }

    private fun save(pets: Models<T>): In<Models<T>> =
        Observable.fromIterable(pets)
            .switchMap { saveOne(it) }
            .collectInto<Models<T>>(mutableListOf()) { result, model -> result as MutableList += model }
            .toObservable()
}
