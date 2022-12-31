package com.kaiqkt.commons.security.auth.config

import com.kaiqkt.commons.security.auth.filter.AuthFilter
import com.kaiqkt.commons.security.auth.properties.AuthProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.HttpStatusEntryPoint

@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties(AuthProperties::class)
class SecurityWebServletConfiguration(
    private val authProperties: AuthProperties
) : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        http
            .exceptionHandling().apply {
                authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            }.and()
            .addFilter(AuthFilter(authProperties, authenticationManager()))
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .csrf().apply { disable() }.and()
            .headers().apply { disable() }.and()
            .authorizeHttpRequests()
            .antMatchers(HttpMethod.GET, *authProperties.ignoreGetPaths).permitAll()
            .antMatchers(HttpMethod.POST, *authProperties.ignorePostPaths).permitAll()
            .antMatchers(HttpMethod.PUT, *authProperties.ignorePutPaths).permitAll()
            .antMatchers(HttpMethod.DELETE, *authProperties.ignoreDeletePaths).permitAll()
            .antMatchers(*MATCHERS).permitAll()
            .anyRequest().authenticated()
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
            "/health"
        )
    }
}
