package io.madrona.geotimer.util

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers

class GtSchedulers(val main: Scheduler = AndroidSchedulers.mainThread(),
                   val io: Scheduler = Schedulers.io(),
                   val computation: Scheduler = Schedulers.computation())