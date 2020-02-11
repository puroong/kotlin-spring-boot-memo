package com.urssu.bum.incubating.util

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*
import kotlin.collections.HashMap

@Component
class JwtUtil {
    // TODO: 환경변수로 빼기
    private val tokenValidMilliseconds = 1000L * 60 * 60
    private val SECRET_KEY = "secret"

    fun extractUsername(token: String): String {
        return extractClaim(token, Claims::getSubject)
    }

    fun extractExpiration(token: String): Date {
        return extractClaim(token, Claims::getExpiration)
    }

    fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T {
        val claims = extractAllClaims(token)
        return claimsResolver.invoke(claims)
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).body
    }

    private fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).before(Date())
    }

    fun generateToken(userDetails: UserDetails): String {
        val claims: Map<String, Any> = HashMap()
        return createToekn(claims, userDetails.username)
    }

    private fun createToekn(claims: Map<String, Any>, subject: String): String {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(Date(System.currentTimeMillis()))
                .setExpiration(Date(System.currentTimeMillis() + tokenValidMilliseconds))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact()
    }

    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return (username.equals(userDetails.username) && !isTokenExpired(token))
    }
}