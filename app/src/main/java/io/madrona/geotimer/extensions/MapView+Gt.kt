package io.madrona.geotimer.extensions

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import io.madrona.geotimer.injector
import io.reactivex.Observable

fun MapView.getMapAsyncObservable(): Observable<GoogleMap> {
    return Observable.create<GoogleMap?> { observer ->
        getMapAsync { map ->
            observer.onNext(map)
            observer.onComplete()
        }
    }.unwrappedOptional().subscribeOn(injector.provideGtSchedulers().main)
}
