package com.urssu.bum.incubating.controller.v1.api

import com.urssu.bum.incubating.controller.v1.request.CreateMemoRequest
import com.urssu.bum.incubating.security.SecurityConstants
import com.urssu.bum.incubating.service.MemoService
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/memo")
class MemoController @Autowired constructor(
    private var memoService: MemoService
){
    @ApiOperation("메모 작성")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createMemo(@RequestHeader(SecurityConstants.HEADER_STRING) authorization: String, @RequestBody createMemoRequest: CreateMemoRequest) = memoService.creatememo(createMemoRequest)
}