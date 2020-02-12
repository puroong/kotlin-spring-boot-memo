package com.urssu.bum.incubating.exception

import org.springframework.http.HttpStatus

class UserNotFoundException : ApiException(HttpStatus.NOT_FOUND, "User Not Found") {

}
