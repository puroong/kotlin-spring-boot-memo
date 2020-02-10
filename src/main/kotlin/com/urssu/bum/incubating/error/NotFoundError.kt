package com.urssu.bum.incubating.error

import org.springframework.http.HttpStatus

open class NotFoundError(override val message: String = "Not Found") : ApiError(HttpStatus.NOT_FOUND, message)