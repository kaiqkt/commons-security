package com.kaiqkt.commons.security.auth.exceptions

import org.springframework.security.core.AuthenticationException

class BadServiceTokenException: AuthenticationException("Service token invalid")