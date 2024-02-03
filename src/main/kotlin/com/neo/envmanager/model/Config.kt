package com.neo.envmanager.model

import com.google.gson.annotations.SerializedName

data class Config(
    @SerializedName("target_path")
    val targetPath: String,
    @SerializedName("current_environment")
    val currentEnv: String? = null,
)