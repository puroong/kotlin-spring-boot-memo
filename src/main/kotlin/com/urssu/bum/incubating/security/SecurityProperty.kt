package com.urssu.bum.incubating.security

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "spring.jwt")
class SecurityProperty(
        val SECRET: String,
        val HEADER_PREFIX: String,
        val EXPIRATION_TIME: Long
)