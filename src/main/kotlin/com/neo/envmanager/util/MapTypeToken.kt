package com.neo.envmanager.util

import com.google.gson.reflect.TypeToken

class MapTypeToken<T> : TypeToken<Map<String, T>>()