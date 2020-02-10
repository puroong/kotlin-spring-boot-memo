package com.urssu.bum.incubating.error

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

open class ApiError(status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR, message: String? = "Internal Server Error") : ResponseStatusException(status, message)