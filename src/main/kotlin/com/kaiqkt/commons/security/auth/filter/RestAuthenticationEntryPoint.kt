package com.kaiqkt.commons.security.auth.filter

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.kaiqkt.commons.security.auth.entities.Error
import com.kaiqkt.commons.security.auth.exceptions.ErrorType
import com.kaiqkt.commons.security.auth.exceptions.JwtExpiredException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component("restAuthenticationEntryPoint")
class RestAuthenticationEntryPoint : AuthenticationEntryPoint {

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authenticationException: AuthenticationException
    ) {
        response.contentType = "application/vnd.kaiqkt_error_v1+json"
        response.status = HttpServletResponse.SC_UNAUTHORIZED

        when (authenticationException) {
            is JwtExpiredException -> {
                val error = Error(ErrorType.ACCESS_TOKEN_EXPIRED, "Access token expired")
                response.outputStream.println(jacksonObjectMapper().writeValueAsString(error))
            }

            is BadCredentialsException -> {
                val error = Error(ErrorType.INVALID_SERVICE_TOKEN, "Invalid service token")
                response.outputStream.println(jacksonObjectMapper().writeValueAsString(error))
            }
            else -> {
                val error = Error(ErrorType.UNKNOWN, authenticationException.message ?: "teste")
                response.outputStream.println(jacksonObjectMapper().writeValueAsString(error))
            }
        }
    }
}
