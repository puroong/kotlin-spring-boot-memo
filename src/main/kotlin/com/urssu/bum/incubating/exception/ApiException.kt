package com.urssu.bum.incubating.exception

import org.springframework.http.HttpStatus
import java.lang.RuntimeException

open class ApiException(val status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR, override val message: String = "Internal Server Error") : RuntimeException(message)