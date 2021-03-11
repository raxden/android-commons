package com.raxdenstudios.commons.android.ext

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

private val onStartStub: () -> Unit = {}
private val onErrorStub: (e: Throwable) -> Unit = { _ -> }
private val onSuccessStub: (Any) -> Unit = {}
private val onNextStub: (Any) -> Unit = {}
private val onCompleteStub: () -> Unit = {}

fun <T : Any> Single<T>.subscribeWith(
  onStart: () -> Unit = onStartStub,
  onError: (e: Throwable) -> Unit = onErrorStub,
  onSuccess: (t: T) -> Unit = onSuccessStub
): Disposable = subscribeOn(Schedulers.io())
  .observeOn(AndroidSchedulers.mainThread())
  .doOnSubscribe { onStart() }
  .subscribeBy(
    onError = onError,
    onSuccess = onSuccess
  )

fun <T : Any> Maybe<T>.subscribeWith(
  onStart: () -> Unit = onStartStub,
  onError: (e: Throwable) -> Unit = onErrorStub,
  onSuccess: (t: T) -> Unit = onSuccessStub,
  onComplete: () -> Unit = onCompleteStub
): Disposable = subscribeOn(Schedulers.io())
  .observeOn(AndroidSchedulers.mainThread())
  .doOnSubscribe { onStart() }
  .subscribeBy(
    onError = onError,
    onSuccess = onSuccess,
    onComplete = onComplete
  )

fun <T : Any> Flowable<T>.subscribeWith(
  onStart: () -> Unit = onStartStub,
  onError: (Throwable) -> Unit = onErrorStub,
  onNext: (t: T) -> Unit = onNextStub,
  onComplete: () -> Unit = onCompleteStub
): Disposable = subscribeOn(Schedulers.io())
  .observeOn(AndroidSchedulers.mainThread())
  .doOnSubscribe { onStart() }
  .subscribeBy(
    onError = onError,
    onNext = onNext,
    onComplete = onComplete
  )

fun <T : Any> Observable<T>.subscribeWith(
  onStart: () -> Unit = onStartStub,
  onError: (e: Throwable) -> Unit = onErrorStub,
  onNext: (t: T) -> Unit = onNextStub,
  onComplete: () -> Unit = onCompleteStub
): Disposable = subscribeOn(Schedulers.io())
  .observeOn(AndroidSchedulers.mainThread())
  .doOnSubscribe { onStart() }
  .subscribeBy(
    onError = onError,
    onNext = onNext,
    onComplete = onComplete
  )

fun Completable.subscribeWith(
  onStart: () -> Unit = onStartStub,
  onError: (e: Throwable) -> Unit = onErrorStub,
  onComplete: () -> Unit = onCompleteStub
): Disposable = subscribeOn(Schedulers.io())
  .observeOn(AndroidSchedulers.mainThread())
  .doOnSubscribe { onStart() }
  .subscribeBy(
    onError = onError,
    onComplete = onComplete
  )
