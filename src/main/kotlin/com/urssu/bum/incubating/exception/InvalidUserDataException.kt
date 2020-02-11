package com.urssu.bum.incubating.exception

import org.springframework.http.HttpStatus

class InvalidUserDataException : ApiException(HttpStatus.NOT_FOUND, "Invalid User Data")