package com.example.mad1

data class HiringChallenge(
    val challengeName: String? = null,
    val eligibility: String? = null,
    val registerDeadline: String? = null,
    val firstPhaseDate: String? = null,
    val url: String? = null,
    var registered: Boolean = false
)
