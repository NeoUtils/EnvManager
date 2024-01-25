package com.neo.envmanager.util.extension

import com.github.ajalt.clikt.core.UsageError

val String.json get() = "$this.json"

fun String.toPair(
    separator: String
): Pair<String, String> {

    val tokens = split(separator, limit = 2)

    if (tokens.size != 2) throw UsageError(message = "Invalid property: '$this'")

    return tokens.let { it[0] to it[1] }
}