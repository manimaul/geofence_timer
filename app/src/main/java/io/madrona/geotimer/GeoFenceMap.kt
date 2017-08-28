package io.madrona.geotimer

import android.app.Activity
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CircleOptions
import com.jakewharton.rxbinding2.view.RxView
import io.madrona.geotimer.databinding.ActivityGeoFenceMapBinding
import io.madrona.geotimer.extensions.getMapAsyncObservable
import io.reactivex.functions.BiFunction

const val metersPerMile = 1609.34

class GeoFenceMap : Activity() {

    private val schedulers = injector.provideGtSchedulers()
    private val compositeDisposable = injector.provideCompositeDisposable()

    lateinit var viewBinding: ActivityGeoFenceMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_geo_fence_map)
        viewBinding.mapView.onCreate(savedInstanceState)
        connectViewBindingEvents(viewBinding)
    }

    override fun onStart() {
        super.onStart()
        viewBinding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        viewBinding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        viewBinding.mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        viewBinding.mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewBinding.mapView.onDestroy()
        compositeDisposable.clear()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        viewBinding.mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        viewBinding.mapView.onLowMemory()
    }

    private fun connectViewBindingEvents(binding: ActivityGeoFenceMapBinding) {
        val googleMap = viewBinding.mapView.getMapAsyncObservable()
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
