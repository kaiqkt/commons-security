package com.kaiqkt.commons.security.auth.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.validation.annotation.Validated

@ConfigurationProperties(prefix = "app.auth")
@ConstructorBinding
@Validated
data class AuthProperties(
    val customerAuthSigningSecret: String?,
    val serviceSharedSecret: String?,
    val ignoreGetPaths:Array<String> = emptyArray(),
    val ignorePostPaths: Array<String> = emptyArray(),
    val ignorePutPaths:Array<String> = emptyArray(),
    val ignoreDeletePaths: Array<String> = emptyArray(),
    val ignoreGenericPaths: Array<String> = emptyArray()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AuthProperties

        if (customerAuthSigningSecret != other.customerAuthSigningSecret) return false
        if (serviceSharedSecret != other.serviceSharedSecret) return false
        if (!ignoreGetPaths.contentEquals(other.ignoreGetPaths)) return false
        if (!ignorePostPaths.contentEquals(other.ignorePostPaths)) return false
        if (!ignorePutPaths.contentEquals(other.ignorePutPaths)) return false
        if (!ignoreDeletePaths.contentEquals(other.ignoreDeletePaths)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = customerAuthSigningSecret?.hashCode() ?: 0
        result = 31 * result + (serviceSharedSecret?.hashCode() ?: 0)
        result = 31 * result + ignoreGetPaths.contentHashCode()
        result = 31 * result + ignorePostPaths.contentHashCode()
        result = 31 * result + ignorePutPaths.contentHashCode()
        result = 31 * result + ignoreDeletePaths.contentHashCode()
        return result
    }
}
