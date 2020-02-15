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

    constructor(e: Exception) : this() {
        message = e.message ?: message
        status = HttpStatus.INTERNAL_SERVER_ERROR
    }

    constructor(e: ApiException) : this() {
        message = e.reason ?: message
        status = e.status
    }

    constructor(errorAttributes: MutableMap<String, Any>): this() {
        message = errorAttributes.get("message") as String
        status = HttpStatus.valueOf(errorAttributes.get("status") as Int)
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