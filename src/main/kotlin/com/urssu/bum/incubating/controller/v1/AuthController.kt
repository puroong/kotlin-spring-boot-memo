package com.urssu.bum.incubating.controller.v1

import com.urssu.bum.incubating.dto.request.UserSigninRequestDto
import com.urssu.bum.incubating.dto.request.UserSignupRequestDto
import com.urssu.bum.incubating.dto.model.security.TokenDto
import com.urssu.bum.incubating.dto.response.SigninResponseDto
import com.urssu.bum.incubating.service.AuthService
import io.swagger.annotations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth")
class AuthController @Autowired constructor(
        private val authService: AuthService
){
    @ApiOperation("회원가입")
    @ApiResponses(
            ApiResponse(code = 201, message = "회원가입 성공"),
            ApiResponse(code = 400, message = "잘못된 요청"),
            ApiResponse(code = 409, message = "중복된 유저 이름")
    )
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    fun signup(@RequestBody userSignupRequest: UserSignupRequestDto) = authService.signupNewUser(userSignupRequest)

    @ApiOperation("로그인")
    @ApiResponses(
            ApiResponse(code = 200, message = "로그인 성공", response = SigninResponseDto::class),
            ApiResponse(code = 400, message = "잘못된 요청"),
            ApiResponse(code = 404, message = "유저 정보 없음")
    )
    @PostMapping("/signin")
    @ResponseStatus(HttpStatus.OK)
    fun signin(@RequestBody userSigninRequest: UserSigninRequestDto) = authService.authenticateAndCreateToken(userSigninRequest)
                .map { SigninResponseDto(TokenDto(it)) }
}