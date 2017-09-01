package io.madrona.geotimer

import android.app.Activity
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.jakewharton.rxbinding2.view.RxView
import io.madrona.geotimer.databinding.ActivityGeoFenceMapBinding
import io.madrona.geotimer.extensions.getMapAsyncObservable
import io.reactivex.functions.BiFunction

const val metersPerMile = 1609.34

class GeoFenceMap : Activity() {

    private val schedulers = injector.provideGtSchedulers()
    private val compositeDisposable = injector.provideCompositeDisposable()

    private lateinit var viewBinding: ActivityGeoFenceMapBinding
    private val mapFragment: MapFragment
        get() = fragmentManager.findFragmentById(R.id.map_view) as MapFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_geo_fence_map)
        connectViewBindingEvents(viewBinding)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    private fun connectViewBindingEvents(binding: ActivityGeoFenceMapBinding) {
        val googleMap = mapFragment.getMapAsyncObservable()
        val addGeofence = RxView.clicks(binding.addButton)
                .withLatestFrom(googleMap, BiFunction<Any, GoogleMap, GoogleMap> { _, gMap -> gMap })

        compositeDisposable.add(addGeofence.observeOn(schedulers.main).subscribe { gMap ->
            val center = gMap.cameraPosition.target
            val options = CircleOptions()
                    .center(center)
                    .radius(.25 * metersPerMile)
                    .strokeColor(Color.BLUE)
            gMap.addCircle(options)
        })
    }


}
