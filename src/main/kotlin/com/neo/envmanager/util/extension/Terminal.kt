package com.neo.envmanager.util.extension

import com.github.ajalt.mordant.terminal.ConversionResult
import com.github.ajalt.mordant.terminal.Terminal
import java.io.File

fun Terminal.promptFile(
    text: String,
    default: File? = null,
    mustExist: Boolean = false,
    canBeFile: Boolean = true,
    canBeDir: Boolean = true,
): File {

    return checkNotNull(
        prompt(
            prompt = text,
            default = default
        ) { input ->

            val file = File(input)

            when {

                input.isEmpty() -> {
                    ConversionResult.Invalid("Path cannot be empty")
                }

                input.isBlank() -> {
                    ConversionResult.Invalid("Path cannot be blank")
                }

                mustExist && !file.exists() -> {
                    ConversionResult.Invalid("Path does not exist")
                }

                !canBeFile && file.isFile -> {
                    ConversionResult.Invalid("Path cannot be a file")
                }

                !canBeDir && file.isDirectory -> {
                    ConversionResult.Invalid("Path cannot be a directory")
                }

                else -> ConversionResult.Valid(file)
            }
        }
    )
}
