package io.madrona.geotimer

import android.app.Activity
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.google.android.gms.maps.GoogleMap
import io.madrona.geotimer.databinding.ActivityGeoFenceMapBinding

class GeoFenceMap : Activity() {

    var map: GoogleMap? = null
    set(value) {
        field = value
        if (value != null) {
            setupMap(value)
        }
    }

    lateinit var viewBinding: ActivityGeoFenceMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_geo_fence_map)
        viewBinding.mapView.onCreate(savedInstanceState)
        viewBinding.mapView.getMapAsync {
            map = it
        }
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
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        viewBinding.mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        viewBinding.mapView.onLowMemory()
    }

    private fun setupMap(map: GoogleMap) {

    }
}
