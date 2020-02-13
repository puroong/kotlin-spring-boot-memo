package com.urssu.bum.incubating.controller.v1

import com.urssu.bum.incubating.dto.request.MemoCreateRequestDto
import com.urssu.bum.incubating.dto.request.MemoUpdateRequestDto
import com.urssu.bum.incubating.dto.model.memo.MemoDto
import com.urssu.bum.incubating.dto.response.GetMyMemosResponse
import com.urssu.bum.incubating.dto.response.GetPublicMemosResponse
import com.urssu.bum.incubating.security.SecurityConstant
import com.urssu.bum.incubating.service.MemoService
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
class MemoController @Autowired constructor(
    private val memoService: MemoService
){
    @ApiOperation("내가 작성한 메모들 보기")
    @GetMapping("/api/v1/user/{username}/memos")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("@permissionService.hasReadAnyMemoPermission() or @permissionService.hasReadMyMemoPermission(#username)")
    fun getMyMemos(@RequestHeader(SecurityConstant.HEADER_STRING) authorization: String,
                   @PathVariable("username") username: String,
                   @RequestParam(value = "tag", required = false) tag: String?,
                   @RequestParam(value = "offset", required = false) offset: Long?,
                   @RequestParam(value = "limit", required = false) limit: Int?) = memoService.getPublishedMemos(username, tag, limit, offset)
                                                                                                                .map { MemoDto(it) }
                                                                                                                .collectList().map { GetMyMemosResponse(it) }

    @ApiOperation("공개된 메모들 보기")
    @GetMapping("/api/v1/memos")
    @ResponseStatus(HttpStatus.OK)
    fun getPublicMemos(@RequestHeader(SecurityConstant.HEADER_STRING) authorization: String,
                       @RequestParam(value = "tag", required = false) tag: String?,
                       @RequestParam(value = "offset", required = false) offset: Long?,
                       @RequestParam(value = "limit", required = false) limit: Int?) = memoService.getPublicMemos(tag, limit, offset)
                                                                                                                    .map { MemoDto(it) }
                                                                                                                    .collectList().map { GetPublicMemosResponse(it) }
    
    @ApiOperation("메모 작성")
    @PostMapping("/api/v1/memo")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("@permissionService.hasWriteMemoPermission()")
    fun createMemo(@RequestHeader(SecurityConstant.HEADER_STRING) authorization: String,
                   @RequestBody createMemoRequest: MemoCreateRequestDto) = memoService.createMemo(createMemoRequest)

    @ApiOperation("메모 수정하기")
    @PutMapping("/api/v1/memo/{memoId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("@permissionService.hasModifyAnyMemoPermission() or @permissionService.hasModifyMyMemoPermission(#authorization, #memoId)")
    fun updateMemo(@RequestHeader(SecurityConstant.HEADER_STRING) authorization: String,
                   @PathVariable("memoId") memoId: Long,
                   @RequestBody updateMemoRequest: MemoUpdateRequestDto) = memoService.updateMemo(memoId, updateMemoRequest)

    @ApiOperation("메모 삭제하기")
    @DeleteMapping("/api/v1/memo/{memoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("@permissionService.hasDeleteAnyMemoPermission() or @permissionService.hasDeleteMyMemoPermission(#authorization, #memoId)")
    fun deleteMemo(@RequestHeader(SecurityConstant.HEADER_STRING) authorization: String,
                   @PathVariable("memoId") memoId: Long) = memoService.deleteMemo(memoId)
}