package com.urssu.bum.incubating.controller.v1

import com.urssu.bum.incubating.dto.request.UserRoleUpdateRequestDto
import com.urssu.bum.incubating.dto.model.user.UserDto
import com.urssu.bum.incubating.dto.response.GetUserResponse
import com.urssu.bum.incubating.security.SecurityConstant
import com.urssu.bum.incubating.security.service.PermissionService
import com.urssu.bum.incubating.service.UserService
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
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
    @ApiResponses(
            ApiResponse(code = 200, message = "유저 조회 성공", response = GetUserResponse::class),
            ApiResponse(code = 400, message = "잘못된 요청"),
            ApiResponse(code = 401, message = "인증 안됨"),
            ApiResponse(code = 403, message = "권한 없음"),
            ApiResponse(code = 404, message = "유저 없음")
    )
    @GetMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("@permissionService.hasReadAnyUserPermission() or @permissionService.hasReadMyUserPermission(#username)")
    fun getUser(@RequestHeader(SecurityConstant.HEADER_STRING) authorization: String,
                @PathVariable("username") username: String) = userService.getUser(username).map { GetUserResponse(UserDto(it)) }

    @ApiOperation("유저 탈퇴")
    @ApiResponses(
            ApiResponse(code = 204, message = "유저 탈퇴 성공"),
            ApiResponse(code = 400, message = "잘못된 요청"),
            ApiResponse(code = 401, message = "인증 안됨"),
            ApiResponse(code = 403, message = "권한 없음"),
            ApiResponse(code = 404, message = "유저 없음")
    )
    @DeleteMapping("/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("@permissionService.hasDeleteAnyUserPermission() or @permissionService.hasDeleteMyUserPermission(#username)")
    fun disableUser(@RequestHeader(SecurityConstant.HEADER_STRING) authorization: String,
                    @PathVariable("username") username: String) = userService.disableUser(username)

    @ApiOperation("유정 권한 변경")
    @ApiResponses(
            ApiResponse(code = 200, message = "유저 권한 변경 성공"),
            ApiResponse(code = 400, message = "잘못된 요청"),
            ApiResponse(code = 401, message = "인증 안됨"),
            ApiResponse(code = 403, message = "권한 없음"),
            ApiResponse(code = 404, message = "유저 없음")
    )
    @PutMapping("/{username}/role")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("@permissionService.hasModifyAnyUserPermissino()")
    fun updateUserRole(@RequestHeader(SecurityConstant.HEADER_STRING) authorization: String,
                       @PathVariable("username") username: String,
                       @RequestBody userRoleUpdateRequest: UserRoleUpdateRequestDto) = userService.updateUserRole(username, userRoleUpdateRequest)
}