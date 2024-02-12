package com.neo.envmanager.com.neo.envmanager.util.extension

import com.neo.envmanager.model.Environment
import com.neo.envmanager.model.Target

fun Environment.isSynchronized(
    target: Target
): Boolean {

    return read() == target.read().toMap()
}
