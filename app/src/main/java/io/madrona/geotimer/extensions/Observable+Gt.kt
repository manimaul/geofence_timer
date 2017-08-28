package io.madrona.geotimer.extensions

import io.reactivex.Observable


fun <T> Observable<T?>.unwrappedOptional() : Observable<T> = this.filter { it != null }.map { it!! }
