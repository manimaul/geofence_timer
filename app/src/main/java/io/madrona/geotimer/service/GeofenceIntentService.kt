package io.madrona.geotimer.service

import android.app.IntentService
import android.content.Intent
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

private const val name = "MdrGeofenceService"

class GeofenceIntentService: IntentService(name) {
    override fun onHandleIntent(intent: Intent) {
        val geoFenceEvent = GeofencingEvent.fromIntent(intent)
        if (geoFenceEvent.hasError()) {
            return
        }
        when (geoFenceEvent.geofenceTransition) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> enterFences(geoFenceEvent.triggeringGeofences)
            Geofence.GEOFENCE_TRANSITION_EXIT -> exitFences(geoFenceEvent.triggeringGeofences)
            Geofence.GEOFENCE_TRANSITION_DWELL -> {}
        }
    }

    private fun enterFences(fences: List<Geofence>) {
        val ids = fences.map { it.requestId }
    }

    private fun exitFences(fences: List<Geofence>) {
        val ids = fences.map { it.requestId }
    }
}