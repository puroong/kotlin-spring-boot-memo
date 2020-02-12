package com.urssu.bum.incubating.exception

import org.springframework.http.HttpStatus

class MemoNotFoundException : ApiException(HttpStatus.NOT_FOUND, "Memo Not Found")