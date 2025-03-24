package drodobyte.core.rx

import io.reactivex.Observable
import io.reactivex.Observable.just
import io.reactivex.Observer
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.MILLISECONDS
import java.util.concurrent.TimeUnit.MINUTES
import java.util.concurrent.TimeUnit.SECONDS

/**
 * Read only values
 */
typealias In<T> = Observable<T>

/**
 * Write only values
 */
typealias Out<T> = Observer<T>

/**
 * Read/Write values
 */
typealias InOut<T> = Subject<T>

/**
 * Read event
 */
typealias In_ = In<Unit>

/**
 * Write event
 */
typealias Out_ = Out<Unit>

/**
 * Read/Write event
 */
typealias InOut_ = InOut<Unit>

/**
 * Just one emit
 */
val <T> In<T>.once: In<T> get() = take(1)

/**
 * No more emits
 */
val <T> In<T>.ignore: In<T> get() = filter { false }

/**
 * Emit items if [cond] is true
 */
fun <T> In<T>.`if`(cond: (T) -> Boolean): In<T> = filter { cond(it) }

/**
 * Emit event if item is true
 */
val In<Boolean>.ifTrue: In_ get() = `if` { it }.map { }

/**
 * Emit event if item is false
 */
val In<Boolean>.ifFalse: In_ get() = `if` { !it }.map { }

/**
 * Flattens emitter
 */
val <T> In<out Iterable<T>>.flat: In<out T> get() = flatMapIterable { it }

/**
 * Pairs with previous emitted item
 */
val <T> In<T>.withPrev: In<Pair<T, T>>
    get() = buffer(2, 1).filter { it.size == 2 }.map { it[0] to it[1] }

/**
 * In pairs of twp emitted items
 */
val <T> In<T>.pair: In<Pair<T, T>>
    get() = buffer(2, 2).filter { it.size == 2 }.map { it[0] to it[1] }

/**
 * Emits only when distinct from previous
 */
val <T> In<T>.distinct: In<T> get() = distinctUntilChanged()

/**
 * Delays stream a number of units
 */
fun In<out Number>.delay(unit: TimeUnit): In<out Number> =
    switchMap { just(it).delay(it.toLong(), unit) }

/**
 * Delays stream a number of milliseconds
 */
val In<out Number>.delayMilli: In<out Number> get() = delay(MILLISECONDS)

/**
 * Delays stream a number of seconds
 */
val In<out Number>.delaySeconds: In<out Number> get() = delay(SECONDS)

/**
 * Delays stream a number of minutes
 */
val In<out Number>.delayMinutes: In<out Number> get() = delay(MINUTES)

fun <T> In<T>.delayMilli(seconds: In<out Number>): In<T> = delay(seconds, MILLISECONDS)
fun <T> In<T>.delaySeconds(seconds: In<out Number>): In<T> = delay(seconds, SECONDS)
fun <T> In<T>.delayMinutes(seconds: In<out Number>): In<T> = delay(seconds, MINUTES)

fun <T> In<T>.delay(time: In<out Number>, unit: TimeUnit): In<T> =
    switchMap { t -> time.delay(unit).map { t } }

/**
 * Combine latest [other] into a [Pair]
 */
operator fun <T, S> In<T>.times(other: In<S>): In<Pair<T, S>> =
    Observable.combineLatest(this, other) { o1: T, o2: S -> o1 to o2 }

/**
 * Concat to [other] In
 */
operator fun <T> In<T>.div(other: In<T>): In<T> = concatMap { other }

/**
 * Merge with [other]
 */
operator fun <T> In<T>.plus(other: In<T>): In<T> = mergeWith(other)

/**
 * Creates a [InOut]. [cacheLast] Caches the latest element observed for future subscribers.
 */
fun <T> io(cacheLast: Boolean = false): InOut<T> = _io(cacheLast)

/**
 * Creates a cached [InOut] and emits [item]
 */
fun <T> io(item: T): InOut<T> = io<T>(cacheLast = true).also { it + item }

/**
 * Creates a [InOut] for events. [cacheLast] Caches the latest event observed for future subscribers.
 */
fun io_(cacheLast: Boolean = false): InOut_ = io(cacheLast)

/**
 * Emits [item]
 */
operator fun <T> InOut<T>.plus(item: T): InOut<T> = apply { onNext(item!!) }

/**
 * Emits [item]
 */
operator fun <T> Out<T>.plus(item: T): Out<T> = this as InOut<T> + item

fun <T> In<T>.act(action: (T) -> Unit): In_ =
    switchMap { item -> Observable.fromCallable { action(item) } }

fun <T> In<T>.act(out: Out<T>): In_ =
    switchMap { item -> Observable.fromCallable { out.onNext(item) } }

/**
 * Runs [action] on error. It terminates the source
 */
@Deprecated(message = "Deprecated in favor of a recoverable one", replaceWith = ReplaceWith("catch"))
fun <T> In<T>.onError(action: (Throwable) -> Unit): In_ =
    map { }.onErrorResumeNext { e: Throwable -> just(e).act { action(it) } }

/**
 * Same as [onError] but recovers from error (does not terminate the source)
 */
fun <T> In<T>.catch(action: (Throwable) -> Unit): In<T> =
    retryWhen { e -> e.act(action) }

/**
 * Subscribes on [Schedulers.io]
 */
val <T> In<T>.onIo: In<T> get() = subscribeOn(Schedulers.io())

private fun <T> _io(cache: Boolean) = if (cache) BehaviorSubject.create<T>() else PublishSubject.create<T>()
