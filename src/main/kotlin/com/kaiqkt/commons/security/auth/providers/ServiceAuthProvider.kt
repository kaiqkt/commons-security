package com.kaiqkt.commons.security.auth.providers

import com.kaiqkt.commons.security.auth.exceptions.BadServiceTokenException
import com.kaiqkt.commons.security.auth.token.CustomAuthentication
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority

const val ROLE_SERVICE = "ROLE_SERVICE"

class ServiceAuthProvider(private val sharedSecret: String) {
    fun handleServiceAuth(authentication: CustomAuthentication): Authentication {
        if (authentication.credentials == sharedSecret) {
            val authority = SimpleGrantedAuthority(ROLE_SERVICE)

            authentication.isAuthenticated = true
            authentication.authorities.add(authority)

            return authentication
        }
        throw BadServiceTokenException()
    }

}