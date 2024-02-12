package com.neo.envmanager.exception.error

import com.github.ajalt.clikt.core.CliktError

class CanNotFindEnvironments : CliktError(
    message = "✖ Can not find environments"
)