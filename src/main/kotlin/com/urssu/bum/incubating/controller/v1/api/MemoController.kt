package com.urssu.bum.incubating.controller.v1.api

import com.urssu.bum.incubating.controller.v1.request.CreateMemoRequest
import com.urssu.bum.incubating.controller.v1.request.UpdateMemoRequest
import com.urssu.bum.incubating.dto.model.memo.MemoDto
import com.urssu.bum.incubating.security.SecurityConstants
import com.urssu.bum.incubating.service.MemoService
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
class MemoController @Autowired constructor(
    private var memoService: MemoService
){
    @ApiOperation("내가 작성한 메모들 보기")
    @GetMapping("/user/{username}/memos")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('READ_ALL_MEMO') or hasAuthority('READ_MY_MEMO') and @permissionService.isMemoOwner(#username)")
    fun getMyMemos(@RequestHeader(SecurityConstants.HEADER_STRING) authorization: String,
                   @PathVariable("username") username: String,
                   @RequestParam(value = "tag", required = false) tag: String?,
                   @RequestParam(value = "offset", required = false) offset: Long?,
                   @RequestParam(value = "limit", required = false) limit: Int?) = memoService.getPublishedMemos(username, tag, limit, offset)

    @ApiOperation("공개된 메모들 보기")
    @GetMapping("/memos")
    @ResponseStatus(HttpStatus.OK)
    fun getPublicMemos(@RequestHeader(SecurityConstants.HEADER_STRING) authorization: String,
                       @RequestParam(value = "tag", required = false) tag: String?,
                       @RequestParam(value = "offset", required = false) offset: Long?,
                       @RequestParam(value = "limit", required = false) limit: Int?) = memoService.getPublicMemos(tag, limit, offset)
    @ApiOperation("메모 작성")
    @PostMapping("/memo")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('WRITE_MEMO')")
    fun createMemo(@RequestHeader(SecurityConstants.HEADER_STRING) authorization: String,
                   @RequestBody createMemoRequest: CreateMemoRequest) = memoService.createMemo(createMemoRequest)

    @ApiOperation("메모 수정하기")
    @PutMapping("/memo/{memoId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('MODIFY_ALL_MEMO') or hasAuthority('MODIFY_MY_MEMO') and @permissionService.isMemoOwner(#authorization, #memoId)")
    fun updateMemo(@RequestHeader(SecurityConstants.HEADER_STRING) authorization: String,
                   @PathVariable("memoId") memoId: Long,
                   @RequestBody updateMemoRequest: UpdateMemoRequest) = memoService.updateMemo(memoId, updateMemoRequest)

    @ApiOperation("메모 삭제하기")
    @DeleteMapping("/memo/{memoId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('DELETE_ALL_MEMO') or hasAuthority('DELETE_MY_MEMO') and @permissionService.isMemoOwner(#authorization, #memoId)")
    fun deleteMemo(@RequestHeader(SecurityConstants.HEADER_STRING) authorization: String,
                   @PathVariable("memoId") memoId: Long) = memoService.deleteMemo(memoId)
}