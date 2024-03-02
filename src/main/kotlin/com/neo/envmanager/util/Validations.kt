package com.neo.envmanager.com.neo.envmanager.util

import com.github.ajalt.clikt.parameters.arguments.ArgumentTransformContext
import com.github.ajalt.mordant.terminal.ConversionResult
import com.neo.envmanager.util.Constants
import java.io.File

val NotBlankValidation = { input: String ->
    if (input.isBlank()) {
        ConversionResult.Invalid("Cannot be blank")
    } else {
        ConversionResult.Valid(input)
    }
}

val EnvironmentsFileValidation: ArgumentTransformContext.(File) -> Unit = {
    if (!it.name.endsWith(Constants.DOT_ENVM)) {
        fail("Invalid extension '${it.extension}'")
    }
}