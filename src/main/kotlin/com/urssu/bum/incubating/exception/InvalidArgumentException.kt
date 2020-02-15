package com.urssu.bum.incubating.exception

import org.springframework.http.HttpStatus

class InvalidArgumentException : ApiException(HttpStatus.BAD_REQUEST, "Invalid Argument")