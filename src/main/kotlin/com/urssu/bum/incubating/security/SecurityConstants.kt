package com.urssu.bum.incubating.security

object SecurityConstants {
    // TODO: 환경변수로 빼기
    const val SECRET = "secret"
    const val TOKEN_PREFIX = "Bearer "
    const val HEADER_STRING = "Authorization"
    const val EXPIRATION_TIME = 1000L * 60 * 30
}