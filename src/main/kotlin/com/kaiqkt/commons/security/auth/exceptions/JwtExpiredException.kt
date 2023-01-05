package com.kaiqkt.commons.security.auth.exceptions

import org.springframework.security.core.AuthenticationException

class JwtExpiredException: AuthenticationException("Access token expired")