package com.neo.envmanager.util

import com.google.gson.Gson

val gson = Gson()
    .newBuilder()
    .setPrettyPrinting()
    .create()