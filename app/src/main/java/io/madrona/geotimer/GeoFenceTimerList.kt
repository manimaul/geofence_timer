package io.madrona.geotimer

import android.app.Activity
import android.os.Bundle

class GeoFenceTimerList : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_geo_fence_timer_list)
    }
}
