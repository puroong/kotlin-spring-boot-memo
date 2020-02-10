package com.urssu.bum.incubating.controller

import com.urssu.bum.incubating.model.dto.auth.SignupUserDTO
import com.urssu.bum.incubating.model.dto.request.SignupRequestDTO
import com.urssu.bum.incubating.service.AuthService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/auth")
class AuthController @Autowired constructor(
        private var userService: AuthService
){
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    fun signup(@Valid @RequestBody signupRequestDTO: SignupRequestDTO) = userService.signup(SignupUserDTO.fromSignupRequest(signupRequestDTO))
}