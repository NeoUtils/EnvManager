package com.neo.envmanager.com.neo.envmanager.util

import java.io.OutputStream
import java.io.PrintStream

fun <R> mutedErrorOutput(
    function: () -> R
) : R {
    val old = System.err

    System.setErr(PrintStream(OutputStream.nullOutputStream()))

    return function().also {
        System.setErr(old)
    }
}