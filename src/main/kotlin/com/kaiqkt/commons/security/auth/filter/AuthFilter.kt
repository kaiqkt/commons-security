package com.kaiqkt.commons.security.auth.filter

import com.kaiqkt.commons.security.auth.properties.AuthProperties
import com.kaiqkt.commons.security.auth.providers.CustomerAuthProvider
import com.kaiqkt.commons.security.auth.providers.ServiceAuthProvider
import com.kaiqkt.commons.security.auth.token.CustomAuthentication
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


const val BEARER_PREFIX = "Bearer "
const val REFRESH_TOKEN_PREFIX = "Refresh-Token"
private const val SESSION_VALIDATE_PATH = "/auth/validate"

class AuthFilter(
    private val authProperties: AuthProperties,
    authenticationManager: AuthenticationManager,
) : BasicAuthenticationFilter(authenticationManager) {

    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val path = request.servletPath
        val accessTokenHeader = request.getHeader(AUTHORIZATION)
        val refreshTokenHeader = request.getHeader(REFRESH_TOKEN_PREFIX)

        if (accessTokenHeader.isNullOrBlank()) {
            chain.doFilter(request, response)
        } else {
            try {
                val customerAuth = CustomAuthentication(accessTokenHeader)

                val authResult = when {
                    !refreshTokenHeader.isNullOrBlank() && accessTokenHeader.startsWith(BEARER_PREFIX) && path.startsWith(SESSION_VALIDATE_PATH) -> {
                        customerAuth.refreshToken = refreshTokenHeader
                        CustomerAuthProvider(authProperties).handleCustomerAuth(customerAuth, true)
                    }

                    accessTokenHeader.startsWith(BEARER_PREFIX) && !path.startsWith(SESSION_VALIDATE_PATH) -> CustomerAuthProvider(
                        authProperties
                    ).handleCustomerAuth(customerAuth, false)

                    else -> ServiceAuthProvider(authProperties).handleServiceAuth(customerAuth)
                }

                SecurityContextHolder.getContext().authentication = authResult

                onSuccessfulAuthentication(request, response, authResult)
            } catch (e: AuthenticationException) {
                SecurityContextHolder.clearContext()
                onUnsuccessfulAuthentication(request, response, e)
            } finally {
                chain.doFilter(request, response)
            }
        }
    }
}