package com.urssu.bum.incubating.exception

import org.springframework.http.HttpStatus

class UserAlreadyExistException : ApiException(HttpStatus.CONFLICT, "User Already Exists")