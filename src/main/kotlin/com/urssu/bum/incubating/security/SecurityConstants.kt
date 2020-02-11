package com.urssu.bum.incubating.security

object SecurityConstants {
    // TODO: 환경변수로 빼기
    val SECRET = "secret"
    val TOKEN_PREFIX = "Bearer "
    val HEADER_STRING = "Authorization"
    val EXPIRATION_TIME = 1000L * 60 * 30
}