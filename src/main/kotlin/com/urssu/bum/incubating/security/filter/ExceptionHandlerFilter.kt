package com.urssu.bum.incubating.security.filter

import com.fasterxml.jackson.databind.ObjectMapper
import com.urssu.bum.incubating.dto.exception.ExceptionDto
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class ExceptionHandlerFilter : OncePerRequestFilter() {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        try {
            filterChain.doFilter(request, response)
        } catch (e: Exception) {
            val exceptionDto = ExceptionDto(e)

            response.status = exceptionDto.status.value()
            response.writer.write(convertObjectToJson(exceptionDto))
        }
    }

    fun convertObjectToJson(obj: Any): String {
        return ObjectMapper().writeValueAsString(obj)
    }
}