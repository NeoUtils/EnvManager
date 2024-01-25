package com.neo.envmanager.util.extension

import com.github.ajalt.clikt.parameters.arguments.ProcessedArgument
import com.github.ajalt.clikt.parameters.arguments.RawArgument
import com.github.ajalt.clikt.parameters.arguments.transformAll

fun RawArgument.properties(
    required: Boolean = false
): ProcessedArgument<List<Pair<String, String>>, String> {

    return transformAll(
        nvalues = -1,
        required = required
    ) { inputs ->
        inputs.map { property ->
            property.toPair(separator = "=")
        }
    }
}
