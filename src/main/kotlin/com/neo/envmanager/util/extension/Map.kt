package com.neo.envmanager.util.extension

fun <K, V> Map<K, V>.iterable(): Iterable<Pair<K, V>> {

    return object : Iterable<Pair<K, V>> {

        override fun iterator() = this@iterable.iterator()
    }
}

operator fun <K, V> Map<K, V>.iterator(): Iterator<Pair<K, V>>  {

    return object : Iterator<Pair<K, V>> {

        private val iterator = entries.iterator()

        override fun hasNext(): Boolean {
            return iterator.hasNext()
        }

        override fun next(): Pair<K, V> {
            val next = iterator.next()
            return next.key to next.value
        }
    }
}