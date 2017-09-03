package io.madrona.geotimer.fragment

import android.app.Activity
import android.os.Bundle
import io.madrona.geotimer.R

class GeoFenceTimerList : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_geo_fence_timer_list)
    }
}
