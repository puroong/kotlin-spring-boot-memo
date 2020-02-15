package com.urssu.bum.incubating.controller.v1

import com.urssu.bum.incubating.dto.model.memo.TagDto
import com.urssu.bum.incubating.dto.response.GetTagsResponse
import com.urssu.bum.incubating.security.SecurityConstant
import com.urssu.bum.incubating.service.TagService
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
class TagController @Autowired constructor(
        private val tagService: TagService
){
    @ApiOperation("사용된 태그들 조회")
    @ApiResponses(
            ApiResponse(code = 200, message = "태그 조회 성공", response = GetTagsResponse::class),
            ApiResponse(code = 400, message = "잘못된 요청"),
            ApiResponse(code = 401, message = "인증 안됨"),
            ApiResponse(code = 403, message = "권한 없음")
    )
    @GetMapping("/api/v1/tags")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("@permissionService.hasReadAnyMemoPermission()")
    fun getTags(@Valid @RequestHeader(SecurityConstant.HEADER_STRING) authorization: String,
                @RequestParam(value = "offset", required = false) offset: Int?,
                @RequestParam(value = "limit") limit: Int) = tagService.getTags(limit, offset)
            .map { TagDto(it) }
            .collectList().map { GetTagsResponse(it) }
}