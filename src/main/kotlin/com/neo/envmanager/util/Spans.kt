package com.neo.envmanager.util

import com.github.ajalt.mordant.rendering.Span
import com.github.ajalt.mordant.rendering.TextStyle
import com.neo.envmanager.util.Patterns.WhiteSpaceRegex
import com.neo.envmanager.util.Patterns.WordOrWhiteSpaceRegex

fun spansOf(
    vararg spans: Pair<String, TextStyle>
): List<Span> {

    return spans.flatMap { span ->
        WordOrWhiteSpaceRegex.findAll(span.first).map {
            if (WhiteSpaceRegex.matches(it.value)) {
                Span.space(it.value.length, span.second)
            } else {
                Span.word(it.value, span.second)
            }
        }
    }
}
