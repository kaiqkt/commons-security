package com.kaiqkt.commons.security.auth.filter

import com.kaiqkt.commons.security.auth.properties.AuthProperties
import com.kaiqkt.commons.security.auth.providers.CustomerAuthProvider
import com.kaiqkt.commons.security.auth.providers.ServiceAuthProvider
import com.kaiqkt.commons.security.auth.token.CustomAuthentication
import com.kaiqkt.commons.security.exceptions.SecretNotProvidedException
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

class AuthFilter(
    private val authProperties: AuthProperties,
    authenticationManager: AuthenticationManager,
    private val restAuthenticationEntryPoint: RestAuthenticationEntryPoint
) : BasicAuthenticationFilter(authenticationManager) {

    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val accessTokenHeader = request.getHeader(AUTHORIZATION)

        if (accessTokenHeader.isNullOrBlank()) {
            chain.doFilter(request, response)
        } else {
            try {
                val customerAuth = CustomAuthentication(accessTokenHeader)

                val authResult = when {
                    accessTokenHeader.startsWith(BEARER_PREFIX) -> {
                        val customerAuthSecret = authProperties.customerAuthSigningSecret ?: throw SecretNotProvidedException("Customer")

                        CustomerAuthProvider(
                            customerAuthSecret
                        ).handleCustomerAuth(customerAuth)

                    }
                    else -> {
                        val serviceSharedSecret = authProperties.serviceSharedSecret ?: throw SecretNotProvidedException("Service")

                        ServiceAuthProvider(serviceSharedSecret).handleServiceAuth(customerAuth)
                    }
                }

                SecurityContextHolder.getContext().authentication = authResult
                onSuccessfulAuthentication(request, response, authResult)
                chain.doFilter(request, response)

            } catch (e: AuthenticationException) {
                SecurityContextHolder.clearContext()
                onUnsuccessfulAuthentication(request, response, e)
                restAuthenticationEntryPoint.commence(request, response, e)
            }
        }
    }
}