package com.neo.envmanager.com.neo.envmanager.util

import com.github.ajalt.mordant.terminal.ConversionResult

val NotBlankValidation = { input: String ->
    if (input.isBlank()) {
        ConversionResult.Invalid("Cannot be blank")
    } else {
        ConversionResult.Valid(input)
    }
}