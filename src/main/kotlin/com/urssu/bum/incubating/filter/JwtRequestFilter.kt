package com.urssu.bum.incubating.filter

import com.urssu.bum.incubating.service.MyUserDeatilsService
import com.urssu.bum.incubating.util.JwtUtil
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
        private val userDetailsService: MyUserDeatilsService,
        private val jwtUtil: JwtUtil
) : OncePerRequestFilter() {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val authorizationHeader = request.getHeader("Authorization")

        var username: String? = null
        var jwt: String? = null

        // TODO: 예외처리 하기
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7)
            username = jwtUtil.extractUsername(jwt)
        }

        if(username != null && SecurityContextHolder.getContext().authentication == null) {
            val userDetails = userDetailsService.loadUserByUsername(username)
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