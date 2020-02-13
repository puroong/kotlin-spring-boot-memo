package com.urssu.bum.incubating.dto.exception

import com.urssu.bum.incubating.exception.ApiException
import org.springframework.http.HttpStatus
import java.util.*

class ExceptionDto() {
    var message: String = HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase
    var status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR
    val error: Int by lazy {
        status.value()
    }
    val timestamp: Date = Date(System.currentTimeMillis())

    constructor(e: Throwable) : this() {
        when (e) {
            is ApiException -> {
                val apiException = e as ApiException
                message = apiException.message
                status = apiException.status
            }
            is org.springframework.security.access.AccessDeniedException -> {
                val accessDeniedException = e as org.springframework.security.access.AccessDeniedException
                message = accessDeniedException.message ?: HttpStatus.FORBIDDEN.reasonPhrase
                status = HttpStatus.FORBIDDEN
            }
            else -> {
                message = e.message ?: message
            }
        }
    }

    constructor(errorAttributes: MutableMap<String, Any>): this() {
        message = errorAttributes.get("message") as String
        status = HttpStatus.valueOf(errorAttributes.get("status") as Int)
    }

    fun create(e: Throwable): ExceptionDto {
        return when (e) {
            is ApiException -> {
                ExceptionDto(e as ApiException)
            }
            is AccessDeniedException -> ExceptionDto()
            else -> ExceptionDto(e)
        }
    }

    fun toErrorAttribute(): MutableMap<String, Any> {
        val errorAttributes = HashMap<String, Any>()
        errorAttributes.put("message", message)
        errorAttributes.put("status", status)
        errorAttributes.put("error", error)
        errorAttributes.put("timestamp", timestamp)

        return errorAttributes
    }
}