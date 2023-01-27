package com.kaiqkt.commons.security.auth

const val ROLE_USER = "ROLE_BUYER"
const val ROLE_SERVICE = "ROLE_SERVICE"

const val AUTHORIZE_USER = "hasRole('$ROLE_USER')"
const val AUTHORIZE_SERVICE = "hasRole('$ROLE_SERVICE')"