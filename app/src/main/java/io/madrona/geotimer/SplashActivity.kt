package io.madrona.geotimer

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.jakewharton.rxbinding2.view.RxView
import io.madrona.geotimer.databinding.ActivitySplashBinding

class SplashActivity : Activity() {

    private val schedulers = injector.provideGtSchedulers()
    private val compositeDisposable = injector.provideCompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding: ActivitySplashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        connectViewBindingClicks(viewBinding)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    private fun connectViewBindingClicks(binding: ActivitySplashBinding) {
        compositeDisposable.add(
                RxView.clicks(binding.geofenceListButton).map<Class<out Activity>> {
                    GeoFenceTimerList::class.java
                }.mergeWith(RxView.clicks(binding.geofenceMapButton).map {
                    GeoFenceMap::class.java
                }).observeOn(schedulers.main).subscribe {
                    startActivity(Intent(this, it))
                })
    }
}
