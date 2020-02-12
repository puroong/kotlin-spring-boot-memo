package com.urssu.bum.incubating.exception

import org.springframework.http.HttpStatus

class MemoAlreadyDeletedException : ApiException(HttpStatus.GONE, "Memo Already Deleted")