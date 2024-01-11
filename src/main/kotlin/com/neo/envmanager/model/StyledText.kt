package com.neo.envmanager.model

import com.github.ajalt.mordant.rendering.*
import com.github.ajalt.mordant.terminal.Terminal
import com.neo.envmanager.util.Patterns.BreakLineRegex
import com.neo.envmanager.util.Patterns.WhiteSpaceRegex
import com.neo.envmanager.util.Patterns.WordOrWhiteSpaceRegex

class StyledText(
    private val lines: Lines
) : Widget {

    constructor(
        spans: List<Span>
    ) : this(Lines(listOf(Line(spans))))

    constructor(
        text: String,
        textStyle: TextStyle
    ) : this(
        Lines(
            text.split(BreakLineRegex).map { line ->
                Line(
                    spans = WordOrWhiteSpaceRegex.findAll(line).map {
                        if (WhiteSpaceRegex.matches(it.value)) {
                            Span.space(it.value.length)
                        } else {
                            Span.word(it.value)
                        }
                    }.toList()
                )
            }
        ).withStyle(textStyle)
    )

    override fun measure(t: Terminal, width: Int): WidthRange {
        return WidthRange(lines.width, width)
    }

    override fun render(
        t: Terminal,
        width: Int
    ): Lines {
        return lines
    }
}