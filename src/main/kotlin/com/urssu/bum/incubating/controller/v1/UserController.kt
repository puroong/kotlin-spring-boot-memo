package com.urssu.bum.incubating.controller.v1

import com.urssu.bum.incubating.dto.request.UserRoleUpdateRequestDto
import com.urssu.bum.incubating.dto.model.user.UserDto
import com.urssu.bum.incubating.dto.response.GetUserResponse
import com.urssu.bum.incubating.security.SecurityConstant
import com.urssu.bum.incubating.security.service.PermissionService
import com.urssu.bum.incubating.service.UserService
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/user")
class UserController @Autowired constructor(
        private val userService: UserService,
        private val permissionService: PermissionService
) {
    @ApiOperation("유저 정보 보기")
    @GetMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("@permissionService.hasReadAnyUserPermission() or @permissionService.hasReadMyUserPermission(#username)")
    fun getUser(@RequestHeader(SecurityConstant.HEADER_STRING) authorization: String,
                @PathVariable("username") username: String) = userService.getUser(username).map { GetUserResponse(UserDto(it)) }

    @ApiOperation("유저 탈퇴")
    @DeleteMapping("/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("@permissionService.hasDeleteAnyUserPermission() or @permissionService.hasDeleteMyUserPermission(#username)")
    fun disableUser(@RequestHeader(SecurityConstant.HEADER_STRING) authorization: String,
                    @PathVariable("username") username: String) = userService.disableUser(username)

    @ApiOperation("유정 권한 변경")
    @PutMapping("/{username}/role")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("@permissionService.hasModifyAnyUserPermissino()")
    fun updateUserRole(@RequestHeader(SecurityConstant.HEADER_STRING) authorization: String,
                       @PathVariable("username") username: String,
                       @RequestBody userRoleUpdateRequest: UserRoleUpdateRequestDto) = userService.updateUserRole(username, userRoleUpdateRequest)
}