package drodobyte.core.rx

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.functions.Action
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import java.util.concurrent.TimeUnit.MINUTES
import java.util.concurrent.TimeUnit.SECONDS

/**
 * Read only values
 */
typealias In<T> = Observable<T>

/**
 * Write only vars
 */
typealias Out<T> = Observer<T>

/**
 * Read/Write variables
 */
typealias InOut<T> = Subject<T>

/**
 * In events
 */
typealias In_ = In<Unit>

/**
 * Out events
 */
typealias Out_ = Observer<Unit>

/**
 * In/Out events
 */
typealias InOut_ = InOut<Unit>

/**
 * Complete event
 */
val Out_.done: Out_ get() = apply { onComplete() }

/**
 * Emit event
 */
val Out_.emit: Out_ get() = apply { onNext(Unit) }

/**
 * Alias for [emit]
 */
val Out_.`do`: Out_ get() = emit

/**
 * Emit value
 */
operator fun <T : Any> Out<T>.plus(t: T): Out<T> = apply { onNext(t) }

/**
 * Combine latest into a [Pair]
 */
operator fun <T, S> In<T>.times(other: In<S>): In<Pair<T, S>> =
//    Observable.combineLatest(this, other, BiFunction { t: T, s: S -> t to s })
    Observable.never() // FIXME

/**
 * Concat
 */
operator fun <T> In<T>.div(other: In<T>): In<T> = concatMap { other }

/**
 * Merge
 */
operator fun <T> In<T>.plus(other: In<T>): In<T> = mergeWith(other)

fun <T> In<T>.`do`(out: Out_): In<T> = doOnNext { out.`do` }

// Handy aliases
val Number.delaySeconds: In_ get() = newIn_.delay(toLong(), SECONDS)
val Number.delayMinutes: In_ get() = newIn_.delay(toLong(), MINUTES)
val <T> In<T>.ignore: In<T> get() = ignoreElements().toObservable()
val <T> In<T>.once: In<T> get() = take(1)
val <T> In<Iterable<T>>.flat: In<T> get() = flatMapIterable { it }
val <T> In<T>.pairs: In<Pair<T, T>>
    get() = buffer(2, 1).filter { it.size == 2 }.map { it[0] to it[1] }

// Factories
val <T> T.`in`: In<T> get() = In.just(this)
val <T> Iterable<T>.inFlat: In<T> get() = In.fromIterable(this)
val <T> T.inout: InOut<T> get() = PublishSubject.create<T>()
val <T> T.out: Out<T> get() = inout
val <T> T.newInOut: InOut<T> get() = PublishSubject.create()
val newInOut_: InOut_ get() = Unit.inout
val newIn_: In_ get() = Unit.`in`
val newOut_: Out_ get() = newInOut_

fun <T> In<T>.switchMapAction(run: (t: T) -> Unit): Observable<T> =
    switchMapCompletable { Completable.fromAction { run(it) } }.toObservable<T>()
