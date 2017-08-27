package io.madrona.geotimer

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class GeoFenceMap : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_geo_fence_map)
    }
}
