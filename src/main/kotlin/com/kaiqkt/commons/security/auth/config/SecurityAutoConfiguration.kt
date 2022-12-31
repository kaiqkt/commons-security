package com.kaiqkt.commons.security.auth.config

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.PropertySource

@Configuration(proxyBeanMethods = false)
@PropertySource("classpath:security.properties")
@Import(value = [SecurityWebServletConfiguration::class])
class SecurityAutoConfiguration {
}