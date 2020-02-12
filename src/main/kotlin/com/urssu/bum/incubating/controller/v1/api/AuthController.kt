package com.urssu.bum.incubating.controller.v1.api

import com.urssu.bum.incubating.controller.v1.request.UserSigninRequest
import com.urssu.bum.incubating.controller.v1.request.UserSignupRequest
import com.urssu.bum.incubating.dto.model.security.TokenDto
import com.urssu.bum.incubating.dto.response.SigninResponseDto
import com.urssu.bum.incubating.service.AuthService
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/auth")
class AuthController @Autowired constructor(
        private var authService: AuthService
){
    @ApiOperation("회원가입")
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    fun signup(@Valid @RequestBody userSignupRequest: UserSignupRequest) = authService.signupNewUser(userSignupRequest)

    @ApiOperation("로그인")
    @PostMapping("/signin")
    @ResponseStatus(HttpStatus.OK)
    fun signin(@Valid @RequestBody userSigninRequest: UserSigninRequest) = authService.authenticateAndCreateToken(userSigninRequest)
                .map { SigninResponseDto(it) }
}