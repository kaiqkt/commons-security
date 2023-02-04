package com.kaiqkt.commons.security.exceptions

class SecretNotProvidedException(private val type: String): Exception("$type secret is not provided")