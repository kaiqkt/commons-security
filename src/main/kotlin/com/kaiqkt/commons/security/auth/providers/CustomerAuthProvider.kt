package com.kaiqkt.commons.security.auth.providers

import com.kaiqkt.commons.crypto.jwt.JWTUtils
import com.kaiqkt.commons.security.auth.filter.BEARER_PREFIX
import com.kaiqkt.commons.security.auth.properties.AuthProperties
import com.kaiqkt.commons.security.auth.token.CustomAuthentication
import com.kaiqkt.commons.security.exceptions.SecretNotProvidedException
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority

class CustomerAuthProvider(private val properties: AuthProperties) {

    fun handleCustomerAuth(authentication: CustomAuthentication): Authentication {
        val accessToken = (authentication.credentials as String).replace(BEARER_PREFIX, "")
        val secret =
            properties.customerAuthSigningSecret ?: throw SecretNotProvidedException("Customer secret is not provided")

        val token = JWTUtils.getClaims(accessToken, secret)
        authentication.id = token.id
        token.authorities.map { authentication.authorities.add(SimpleGrantedAuthority(it)) }
        authentication.sessionId = token.sessionId
        authentication.isAuthenticated = !token.expired

        return authentication
    }
}