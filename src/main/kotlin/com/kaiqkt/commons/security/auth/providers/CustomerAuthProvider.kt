package com.kaiqkt.commons.security.auth.providers

import com.kaiqkt.commons.crypto.jwt.JWTUtils
import com.kaiqkt.commons.security.auth.exceptions.JwtExpiredException
import com.kaiqkt.commons.security.auth.filter.BEARER_PREFIX
import com.kaiqkt.commons.security.auth.token.CustomAuthentication
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority

class CustomerAuthProvider(private val secret: String) {

    fun handleCustomerAuth(authentication: CustomAuthentication): Authentication {
        val accessToken = (authentication.credentials as String).replace(BEARER_PREFIX, "")

        val token = try {
            JWTUtils.getClaims(accessToken, secret)
        } catch (ex: Exception) {
            throw JwtExpiredException()
        }

        authentication.id = token.id
        token.authorities.map { authentication.authorities.add(SimpleGrantedAuthority(it)) }
        authentication.sessionId = token.sessionId
        authentication.isAuthenticated = true

        return authentication
    }
}