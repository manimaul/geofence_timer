package io.madrona.geotimer.fragment

import android.Manifest
import android.app.Activity
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.PermissionChecker
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.jakewharton.rxbinding2.view.RxView
import io.madrona.geotimer.R
import io.madrona.geotimer.databinding.ActivityGeoFenceMapBinding
import io.madrona.geotimer.util.injector
import io.madrona.geotimer.extensions.getMapAsyncObservable
import io.reactivex.functions.BiFunction

const val metersPerMile = 1609.34

class GeoFenceMap : Activity() {

    private val schedulers = injector.provideGtSchedulers()
    private val compositeDisposable = injector.provideCompositeDisposable()

    private lateinit var viewBinding: ActivityGeoFenceMapBinding
    private val mapFragment: MapFragment
        get() = fragmentManager.findFragmentById(R.id.map_view) as MapFragment
    private val googleMapBiFunction = BiFunction<Any, GoogleMap, GoogleMap> { _, gMap -> gMap }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_geo_fence_map)
        connectViewBindingEvents(viewBinding)
        connectLocationEnableEvents()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    private fun connectLocationEnableEvents() {
        compositeDisposable.add(io.madrona.geotimer.util.requestPermissions(fragmentManager, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
                .observeOn(schedulers.main)
                .filter { permission ->
                    permission.first().grantResult == PermissionChecker.PERMISSION_GRANTED
                }
                .withLatestFrom(mapFragment.getMapAsyncObservable(), googleMapBiFunction)
                .subscribe { gMap ->
                    gMap.isMyLocationEnabled = true
                })
    }

    private fun connectViewBindingEvents(binding: ActivityGeoFenceMapBinding) {
        compositeDisposable.add(RxView.clicks(binding.addButton)
                .withLatestFrom(mapFragment.getMapAsyncObservable(), googleMapBiFunction)
                .observeOn(schedulers.main)
                .subscribe { gMap ->
                    val center = gMap.cameraPosition.target
                    val options = CircleOptions()
                            .center(center)
                            .radius(.25 * metersPerMile)
                            .strokeColor(Color.BLUE)
                    gMap.addCircle(options)
                })
    }


}
