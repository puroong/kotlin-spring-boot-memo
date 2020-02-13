package com.urssu.bum.incubating.controller.v1

import com.urssu.bum.incubating.dto.model.memo.TagDto
import com.urssu.bum.incubating.dto.response.GetTagsResponse
import com.urssu.bum.incubating.security.SecurityConstant
import com.urssu.bum.incubating.service.TagService
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
class TagController @Autowired constructor(
        private val tagService: TagService
){
    @ApiOperation("내가 작성한 메모들 보기")
    @GetMapping("/api/v1/tags")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("@permissionService.hasReadAnyMemoPermission()")
    fun getTags(@Valid @RequestHeader(SecurityConstant.HEADER_STRING) authorization: String,
                @RequestParam(value = "offset", required = false) offset: Int?,
                @RequestParam(value = "limit") limit: Int) = tagService.getTags(limit, offset)
                                                                                    .map { TagDto(it) }
                                                                                    .collectList().map { GetTagsResponse(it) }
}