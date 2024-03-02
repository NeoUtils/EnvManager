package com.neo.envmanager.com.neo.envmanager.util.extension

import com.neo.envmanager.model.Environment
import com.neo.envmanager.model.Installation
import com.neo.envmanager.util.extension.jsonFiles

fun Installation.environments() = environmentsDir.jsonFiles().map(::Environment)