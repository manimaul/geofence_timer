package io.madrona.geotimer

import io.reactivex.disposables.CompositeDisposable

interface Injector {
    val provideGtSchedulers: () -> GtSchedulers
    val provideCompositeDisposable: () -> CompositeDisposable
}

private class InjectorImpl: Injector {

    override val provideGtSchedulers: () -> GtSchedulers
        get() = { GtSchedulers() }

    override val provideCompositeDisposable: () -> CompositeDisposable
        get() = { CompositeDisposable() }
}


val injector: Injector = InjectorImpl()
