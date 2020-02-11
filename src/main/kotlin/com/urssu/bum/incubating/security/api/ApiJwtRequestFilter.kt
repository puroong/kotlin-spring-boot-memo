package com.urssu.bum.incubating.security.api

import com.urssu.bum.incubating.security.CustomUserDeatilsService
import com.urssu.bum.incubating.security.SecurityConstants
import com.urssu.bum.incubating.security.util.JwtUtil
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
class ApiJwtRequestFilter @Autowired constructor(
        private val userDetailsService: CustomUserDeatilsService,
        private val jwtUtil: JwtUtil
) : OncePerRequestFilter() {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val authorizationHeader = request.getHeader(SecurityConstants.HEADER_STRING)

        var username: String? = null
        var jwt: String? = null

        // TODO: 예외처리 하기
        if(authorizationHeader != null && authorizationHeader.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            jwt = authorizationHeader.substring(SecurityConstants.TOKEN_PREFIX.length)
            username = jwtUtil.extractUsername(jwt)
        }

        if(username != null && SecurityContextHolder.getContext().authentication == null) {
            val userDetails = userDetailsService.loadUserByUsername(username)
            // TODO: jwt!!로 null 여부 판단하는게 좋은 건지 모르겠음
            if(jwtUtil.validateToken(jwt!!, userDetails)) {
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