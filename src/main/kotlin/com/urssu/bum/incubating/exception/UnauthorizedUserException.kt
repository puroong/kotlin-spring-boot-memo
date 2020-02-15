package com.urssu.bum.incubating.exception

import org.springframework.http.HttpStatus

class UnauthorizedUserException : ApiException(HttpStatus.UNAUTHORIZED, "Unauthorized User")
