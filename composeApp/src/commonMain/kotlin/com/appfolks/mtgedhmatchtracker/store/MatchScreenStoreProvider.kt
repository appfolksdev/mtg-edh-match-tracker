package com.appfolks.mtgedhmatchtracker.store

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory

class MatchScreenStoreProvider(
    private val storeFactory: StoreFactory = DefaultStoreFactory(),
) {
    private var store: MatchScreenStore? = null

    fun provide(): MatchScreenStore {
        return store ?: MatchScreenStoreFactory(storeFactory)
            .create()
            .also { store = it }
    }

    fun dispose() {
        store?.dispose()
        store = null
    }
}
