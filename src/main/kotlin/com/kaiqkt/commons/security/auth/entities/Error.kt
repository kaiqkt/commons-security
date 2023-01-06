package com.kaiqkt.commons.security.auth.entities

import com.kaiqkt.commons.security.auth.exceptions.ErrorType

data class Error(
    val type: ErrorType,
    val message: String
)