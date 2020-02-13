package com.urssu.bum.incubating.exception

import org.springframework.http.HttpStatus

class ExpiredTokenException : ApiException(HttpStatus.UNAUTHORIZED, "Expired Token")
