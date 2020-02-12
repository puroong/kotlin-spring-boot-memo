package com.urssu.bum.incubating.controller.v1.api

import com.urssu.bum.incubating.security.SecurityConstants
import com.urssu.bum.incubating.service.UserService
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
class UserController @Autowired constructor(
        private val userService: UserService
) {
    @ApiOperation("유저 정보 보기")
    @GetMapping("/user/{username}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('READ_ALL_USER') or hasAuthority('READ_MY_USER') and @permissionService.isMe(#username)")
    fun getUser(@RequestHeader(SecurityConstants.HEADER_STRING) authorization: String,
                @PathVariable("username") username: String) = userService.getUser(username)

    @ApiOperation("유저 탈퇴")
    @DeleteMapping("/user/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('DELETE_ALL_USER') or hasAuthority('DELETE_MY_USER') and @permissionService.isMe(#username)")
    fun disableUser(@RequestHeader(SecurityConstants.HEADER_STRING) authorization: String,
                    @PathVariable("username") username: String) = userService.disableUser(username)


}