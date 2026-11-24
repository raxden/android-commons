package com.raxdenstudios.commons.coroutines

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Serialises suspend sections so that only one [withLock] block runs at a time.
 *
 * Depend on this interface rather than on [MutexSuspendLock], so tests can substitute a fake.
 *
 * Use one instance per lock, not one per app. Every lock has this same type, so when two unrelated
 * locks are bound in the same DI graph (for example a topic-sync lock and an alarm-schedule lock),
 * tell them apart with qualifiers (for example Koin `named(...)`) or with a dedicated subtype per
 * lock. Share each instance between every caller that must not overlap, such as a read-modify-write
 * started both at app start and from a broadcast receiver.
 *
 * ## Usage
 *
 * ```kotlin
 * class SyncTopicUseCase(private val lock: SuspendLock) {
 *     suspend operator fun invoke(): Topic = lock.withLock { readSubscribeAndSave() }
 * }
 *
 * // One shared instance, e.g. a DI singleton:
 * val topicLock: SuspendLock = MutexSuspendLock()
 * ```
 */
interface SuspendLock {

    /**
     * Runs [block] once no other section holds this lock, and returns its result.
     *
     * The lock is released when [block] returns, throws or is cancelled. Do not call [withLock]
     * on the same lock from inside [block]: [MutexSuspendLock] is not reentrant and such a call
     * suspends forever.
     */
    suspend fun <T> withLock(
        block: suspend () -> T,
    ): T
}

/**
 * [SuspendLock] backed by a kotlinx.coroutines [Mutex].
 *
 * Not reentrant: calling [withLock] from inside its own block suspends forever, because the inner
 * call waits for the mutex that the outer call still holds. Each instance is its own lock, so
 * create one and share it instead of creating one per caller.
 */
class MutexSuspendLock : SuspendLock {

    private val mutex = Mutex()

    override suspend fun <T> withLock(
        block: suspend () -> T,
    ): T = mutex.withLock { block() }
}
