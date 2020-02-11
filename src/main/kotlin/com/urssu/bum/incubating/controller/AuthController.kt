package com.urssu.bum.incubating.controller

import com.urssu.bum.incubating.dto.model.user.UserCredentialDto
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
    fun signup(@Valid @RequestBody userCredentialDto: UserCredentialDto) = authService.signupNewUser(userCredentialDto)

    @ApiOperation("로그인")
    @PostMapping("/signin")
    @ResponseStatus(HttpStatus.OK)
    fun signin(@Valid @RequestBody userCredentialDto: UserCredentialDto): SigninResponseDto {
        val jwt = authService.authenticateAndCreateJwt(userCredentialDto)
        return SigninResponseDto(jwt)
    }
}