package drodobyte.core.rx

import io.reactivex.schedulers.TestScheduler
import java.util.concurrent.TimeUnit

operator fun TestScheduler.plus(seconds: Number): TestScheduler =
    apply { advanceTimeBy(seconds.toLong(), TimeUnit.SECONDS) }
