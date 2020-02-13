package com.urssu.bum.incubating.exception

import org.springframework.http.HttpStatus

class RoleNotFoundException : ApiException(HttpStatus.NOT_FOUND, "Role Not Found") {

}
