package io.madrona.geotimer

interface Injector {
    val provideGtSchedulers: () -> GtSchedulers
}

private class InjectorImpl: Injector {

    override val provideGtSchedulers: () -> GtSchedulers
        get() = { GtSchedulers() }
}


val injector: Injector = InjectorImpl()
