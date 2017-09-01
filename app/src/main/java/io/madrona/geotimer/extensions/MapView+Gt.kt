package io.madrona.geotimer.extensions

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import io.madrona.geotimer.injector
import io.reactivex.Observable


fun MapView.getMapAsyncObservable(): Observable<GoogleMap> = createObservable(this::getMapAsync)

fun MapFragment.getMapAsyncObservable(): Observable<GoogleMap> = createObservable(this::getMapAsync)

private fun createObservable(callback: (OnMapReadyCallback) -> Unit): Observable<GoogleMap> {
    return Observable.create<GoogleMap?> { observer ->
        callback(OnMapReadyCallback { map ->
            observer.onNext(map)
            observer.onComplete()
        })
    }.unwrappedOptional().subscribeOn(injector.provideGtSchedulers().main)
}
