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
import org.springframework.security.core.userdetails.UserDetails
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
    private fun getToken(authorizationHeader: String?): String? {
        if(authorizationHeader != null
                && authorizationHeader.length > securityProperty.HEADER_PREFIX.length
                && authorizationHeader.startsWith(securityProperty.HEADER_PREFIX)) {
            return authorizationHeader.substring(securityProperty.HEADER_PREFIX.length)
        }
        return null
    }

    private fun getUsernameFromToken(token: String): String {
        try {
            return jwtUtil.extractUsername(token)
        } catch (e: Exception) {
            when (e) {
                is MalformedJwtException -> throw MalformedTokenException()
                is ExpiredJwtException -> throw ExpiredTokenException()
                else -> throw e
            }
        }
    }

    private fun validateToken(token: String, userDetails: UserDetails): Boolean {
        try {
            return jwtUtil.validateToken(token, userDetails)
        } catch (e: Exception) {
            when(e) {
                is ExpiredJwtException -> throw ExpiredTokenException()
                else -> throw e
            }
        }
    }

    private fun authorizeUser(request: HttpServletRequest, userDetails: UserDetails) {
        val usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.authorities
        )
        usernamePasswordAuthenticationToken
                .details = WebAuthenticationDetailsSource().buildDetails(request)
        SecurityContextHolder.getContext().authentication = usernamePasswordAuthenticationToken
    }

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {

        var jwt: String? = getToken(request.getHeader(SecurityConstant.HEADER_STRING))

        if (jwt != null && SecurityContextHolder.getContext().authentication == null) {
            var username: String = getUsernameFromToken(jwt)
            val userDetails = userDetailsService.loadUserByUsername(username)

            if (validateToken(jwt, userDetails)) authorizeUser(request, userDetails)
        }

        filterChain.doFilter(request, response)
    }
}