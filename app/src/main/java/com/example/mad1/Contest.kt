package com.example.mad1

data class Contest(
    val contestName: String? = null,
    val platform: String? = null,
    val contestDate: String? = null,
    val time: String? = null,
    val duration: String? = null,
    val contestUrl: String? = null,
    var registered: Boolean = false
)
