package io.madrona.geotimer.util

import android.app.Fragment
import android.app.FragmentManager
import android.os.Build
import android.os.Bundle
import android.support.v13.app.FragmentCompat
import android.support.v4.content.PermissionChecker
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.security.SecureRandom
import java.util.concurrent.ThreadLocalRandom

data class PermissionRequestResult(val permission: String, @PermissionChecker.PermissionResult val grantResult: Int)

/**
 * @return a pseudo-random number between min and max, inclusive.
 */
private const val MIN_16BIT_INT = 0
private const val MAX_16BIT_INT = 65535
private fun rand16BitInt(): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        ThreadLocalRandom.current().nextInt(MIN_16BIT_INT, MAX_16BIT_INT + 1)
    } else {
        SecureRandom().nextInt(MAX_16BIT_INT - MIN_16BIT_INT + 1) + MIN_16BIT_INT
    }
}

private val TAG = PermissionRequestBroker::class.java.simpleName
private const val KEY_PERMISSIONS = "KEY_PERMISSIONS"
private const val KEY_REQUEST_CODE = "KEY_REQUEST_CODE"

/**
 * Request runtime permissions and get an {@link Observable<List<PermissionRequestResult>} result.
 *
 * @param fragmentManager The fragment manager
 * @param permissions The permissions to request
 */
fun requestPermissions(fragmentManager: FragmentManager,
                       permissions: Array<String>): Observable<List<PermissionRequestResult>> {
    return Observable.defer {
        val broker = PermissionRequestBroker()
        val args = Bundle()
        args.putStringArray(KEY_PERMISSIONS, permissions)
        args.putInt(KEY_REQUEST_CODE, rand16BitInt())
        broker.arguments = args
        fragmentManager.beginTransaction()
                .add(broker, TAG)
                .commit()
        broker.resultObservable
    }
}

/**
 * Headless retain fragment for brokering an Observable result from requesting permissions.
 */
class PermissionRequestBroker : Fragment() {

    private val resultSubject = PublishSubject.create<List<PermissionRequestResult>>()
    val resultObservable: Observable<List<PermissionRequestResult>>
        get() = resultSubject

    private val requestCode: Int
        get() = arguments.getInt(KEY_REQUEST_CODE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        val arguments = arguments
        val permissions = arguments.getStringArray(KEY_PERMISSIONS)
        FragmentCompat.requestPermissions(this, permissions, requestCode)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == this.requestCode) {
            resultSubject.onNext(grantResults.zip(permissions).map { PermissionRequestResult(it.second, it.first) })
            resultSubject.onComplete()
            fragmentManager
                    .beginTransaction()
                    .remove(this)
                    .commit()
        }
    }
}