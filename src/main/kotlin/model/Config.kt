package com.neo.properties.model

import com.github.ajalt.clikt.core.CliktCommand
import com.google.gson.Gson
import com.neo.properties.Constants
import com.neo.properties.util.extension.tryAddToGitIgnore
import java.io.File

data class Config(
    val properties : String,
)