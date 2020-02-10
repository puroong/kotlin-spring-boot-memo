package com.urssu.bum.incubating.error

import org.springframework.http.HttpStatus

open class ConflictError(message: String) : ApiError(HttpStatus.CONFLICT, message)