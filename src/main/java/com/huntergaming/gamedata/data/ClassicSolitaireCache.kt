package com.huntergaming.gamedata.data

import javax.inject.Inject

class ClassicSolitaireCache<K, V> @Inject constructor() {

    private val cache: MutableMap<K, V> = mutableMapOf()

    fun add(key: K, item: V) {
        cache[key] = item
    }

    fun contains(key: K) = cache.contains(key)

    fun get(key: K): V? = cache[key]
}