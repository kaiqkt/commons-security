package com.kaiqkt.commons.security.auth

const val ROLE_SELLER = "ROLE_SELLER"
const val ROLE_BUYER = "ROLE_BUYER"
const val ROLE_SERVICE = "ROLE_SERVICE"

const val AUTHORIZE_BUYER = "hasRole('$ROLE_BUYER')"
const val AUTHORIZE_SELLER = "hasRole('$ROLE_SELLER')"
const val AUTHORIZE_SERVICE = "hasRole('$ROLE_SERVICE')"
const val AUTHORIZE_SELLER_OR_BUYER = "hasRole('$ROLE_SELLER') or hasRole('$ROLE_BUYER')"