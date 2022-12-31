package com.kaiqkt.commons.security.auth.exceptions

import org.springframework.security.core.AuthenticationException

class JwtTokenExpiredException: AuthenticationException("Token expired")