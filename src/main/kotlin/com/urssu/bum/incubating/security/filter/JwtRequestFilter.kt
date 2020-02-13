package com.urssu.bum.incubating.security.filter

import com.urssu.bum.incubating.exception.ExpiredTokenException
import com.urssu.bum.incubating.exception.MalformedTokenException
import com.urssu.bum.incubating.security.service.CustomUserDeatilsService
import com.urssu.bum.incubating.security.SecurityConstant
import com.urssu.bum.incubating.security.SecurityProperty
import com.urssu.bum.incubating.security.util.JwtTokenUtil
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtRequestFilter @Autowired constructor(
        private val userDetailsService: CustomUserDeatilsService,
        private val jwtUtil: JwtTokenUtil,
        private val securityProperty: SecurityProperty
) : OncePerRequestFilter() {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val authorizationHeader = request.getHeader(SecurityConstant.HEADER_STRING)

        var jwt: String? = null

        if(authorizationHeader != null && authorizationHeader.startsWith(securityProperty.HEADER_PREFIX)) {
            jwt = authorizationHeader.substring(securityProperty.HEADER_PREFIX.length)
        }

        if (jwt != null && SecurityContextHolder.getContext().authentication == null) {
            var username: String? = null
            try {
                username = jwtUtil.extractUsername(jwt)
            } catch (e: Exception) {
                when (e) {
                    is MalformedJwtException -> throw MalformedTokenException()
                    is ExpiredJwtException -> throw ExpiredTokenException()
                    else -> throw e
                }
            }
            val userDetails = userDetailsService.loadUserByUsername(username)

            var isValidToken = false
            try {
                isValidToken = jwtUtil.validateToken(jwt, userDetails)
            } catch (e: Exception) {
                when(e) {
                    is ExpiredJwtException -> throw ExpiredTokenException()
                    else -> throw e
                }
            }

            if (isValidToken) {
                val usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.authorities
                )
                usernamePasswordAuthenticationToken
                        .details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = usernamePasswordAuthenticationToken
            }
        }

        filterChain.doFilter(request, response)
    }
}