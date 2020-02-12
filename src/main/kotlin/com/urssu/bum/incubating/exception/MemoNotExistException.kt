package com.urssu.bum.incubating.exception

import org.springframework.http.HttpStatus

class MemoNotExistException : ApiException(HttpStatus.NOT_FOUND, "Memo Not Exist")