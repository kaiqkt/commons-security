package com.kaiqkt.commons.security.auth

import com.kaiqkt.commons.security.auth.token.CustomAuthentication
import org.springframework.security.core.context.SecurityContextHolder

fun getUserId() = (SecurityContextHolder.getContext().authentication as CustomAuthentication).id!!
fun getSessionId() = (SecurityContextHolder.getContext().authentication as CustomAuthentication).sessionId!!
fun getAccessToken() = (SecurityContextHolder.getContext().authentication as CustomAuthentication).accessToken
fun isAuthenticated() = SecurityContextHolder.getContext().authentication.isAuthenticated
fun isServiceAuth() =
    (SecurityContextHolder.getContext().authentication as CustomAuthentication).authorities.filter { it.authority == ROLE_SERVICE }
fun hasStoreRole() =
    (SecurityContextHolder.getContext().authentication as CustomAuthentication).authorities.filter { it.authority == ROLE_STORE }