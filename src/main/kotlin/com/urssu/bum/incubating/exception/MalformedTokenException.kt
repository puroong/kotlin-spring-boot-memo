package com.urssu.bum.incubating.exception

import org.springframework.http.HttpStatus

class MalformedTokenException : ApiException(HttpStatus.UNAUTHORIZED, "Malformed Token")