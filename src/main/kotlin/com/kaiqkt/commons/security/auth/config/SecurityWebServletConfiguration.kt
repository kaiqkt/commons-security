package com.kaiqkt.commons.security.auth.config

import com.kaiqkt.commons.security.auth.filter.AuthFilter
import com.kaiqkt.commons.security.auth.filter.RestAuthenticationEntryPoint
import com.kaiqkt.commons.security.auth.properties.AuthProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy

@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties(AuthProperties::class)
class SecurityWebServletConfiguration(
    private val authProperties: AuthProperties,
    private val restAuthenticationEntryPoint: RestAuthenticationEntryPoint
) : WebSecurityConfigurerAdapter() {

    override fun configure(web: WebSecurity) {
        web.ignoring().antMatchers(HttpMethod.GET, *authProperties.ignoreGetPaths)
            .antMatchers(HttpMethod.POST, *authProperties.ignorePostPaths)
            .antMatchers(HttpMethod.PUT, *authProperties.ignorePutPaths)
            .antMatchers(HttpMethod.DELETE, *authProperties.ignoreDeletePaths)
            .antMatchers(*MATCHERS)
    }

    override fun configure(http: HttpSecurity) {
        http
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .csrf().apply { disable() }.and()
            .headers().apply { disable() }.and()
            .authorizeHttpRequests()
            .anyRequest().authenticated()
            .and()
            .antMatcher("/ws/**")
            .addFilter(AuthFilter(authProperties, authenticationManager(), restAuthenticationEntryPoint))

    }

    companion object {
        private val MATCHERS = arrayOf(
            "/v2/api-docs",
            "/v3/api-docs/**",
            "/configuration/ui",
            "/swagger-resources/**",
            "/configuration/security",
            "/swagger-ui/index.html",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/webjars/**",
            "/api-docs.yml",
            "/docs",
            "/health",
            "/ws/**"
        )
    }
}
