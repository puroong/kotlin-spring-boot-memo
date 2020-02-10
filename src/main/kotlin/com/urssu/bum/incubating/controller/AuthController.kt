package com.urssu.bum.incubating.controller

import com.urssu.bum.incubating.model.dto.request.SigninRequestDTO
import com.urssu.bum.incubating.model.dto.request.SignupRequestDTO
import com.urssu.bum.incubating.model.dto.response.SigninResponseDTO
import com.urssu.bum.incubating.service.AuthService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/auth")
class AuthController @Autowired constructor(
        private var authService: AuthService
){
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    fun signup(@Valid @RequestBody signupRequestDTO: SignupRequestDTO) = authService.signup(signupRequestDTO.toSignupUserDTO())

    @PostMapping("/signin")
    @ResponseStatus(HttpStatus.OK)
    fun signin(@Valid @RequestBody signinRequestDTO: SigninRequestDTO): SigninResponseDTO {
        val jwt = authService.signinAndCreateJwt(signinRequestDTO.toSigninUserDTO())
        return SigninResponseDTO(jwt)
    }
}