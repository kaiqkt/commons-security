package com.kaiqkt.commons.security.auth

const val ROLE_USER = "ROLE_USER"
const val ROLE_SERVICE = "ROLE_SERVICE"
const val ROLE_STORE = "ROLE_STORE"

const val AUTHORIZE_USER = "hasRole('$ROLE_USER')"
const val AUTHORIZE_SERVICE = "hasRole('$ROLE_SERVICE')"
const val AUTHORIZE_USER_OR_STORE = "hasRole('$ROLE_STORE') or hasRole('$ROLE_USER')"