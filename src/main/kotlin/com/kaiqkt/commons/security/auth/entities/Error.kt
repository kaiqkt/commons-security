package com.kaiqkt.commons.security.auth.entities

data class Error(
    val type: String = "TOKEN_EXPIRED",
    val message: String = "Access token expired"
)