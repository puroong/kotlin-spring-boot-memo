package com.urssu.bum.incubating.config

import com.urssu.bum.incubating.dto.exception.ExceptionDto
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes
import org.springframework.stereotype.Component
import org.springframework.web.context.request.WebRequest

@Component
class ErrorAttributeConfig : DefaultErrorAttributes() {
    override fun getErrorAttributes(webRequest: WebRequest, includeStackTrace: Boolean): MutableMap<String, Any> {
        val throwable: Throwable? = getError(webRequest)
        if (throwable == null) {
            val errorAttributes = super.getErrorAttributes(webRequest, includeStackTrace)
            return createErrorAttributes(errorAttributes)
        }
        else return createErrorAttributes(throwable)
    }

    private fun createErrorAttributes(throwable: Throwable): MutableMap<String, Any> {
        return ExceptionDto(throwable).toErrorAttribute()
    }

    private fun createErrorAttributes(errorAttributes: MutableMap<String, Any>): MutableMap<String, Any> {
        return ExceptionDto(errorAttributes).toErrorAttribute()
    }
}