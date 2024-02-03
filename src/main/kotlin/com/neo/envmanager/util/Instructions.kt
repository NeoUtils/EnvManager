package com.neo.envmanager.util

import com.github.ajalt.mordant.rendering.TextStyles

object Instructions {

    val INSTALL = "Use ${TextStyles.dim("\$ envm install")} to install"
    val SAVE = "Use ${TextStyles.dim("\$ envm save <tag>")} to save the current environment"
}